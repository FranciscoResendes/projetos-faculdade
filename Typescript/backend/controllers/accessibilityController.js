const fs = require('fs');
const path = require('path');
const Website = require('../models/website');
const { QualWeb } = require('@qualweb/core');
const { url } = require('inspector');
const Statistics = require('../models/statistics');
const DetailStatistics = require('../models/detailStatistics');
const reportService = require('./reportController'); // Import the service fil

exports.getStatistics = async (req, res) => {
    try {
        const statistics = await Statistics.find({ idWebsite: req.params.websiteId });
        res.json(statistics);
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: err.message });
    }
};

exports.getDetailedStatistics = async (req, res) => {
    try{
        const detailsStatistics = await DetailStatistics.find({ idWebsite: req.params.websiteId, idPage: req.params.pageId });
        res.json(detailsStatistics);
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: err.message });
    }
};


exports.evaluateWebsiteAccessibility = async (req, res) => {
    const websiteId = req.body.website._id;
    const pages = req.body.pages; // Array of page URLs to evaluate

    // Create QualWeb instance
    const plugins = { adBlock: false, stealth: true };
    const clusterOptions = { timeout: 60 * 1000 };
    const launchOptions = {args: ['--no-sandbox', '--ignore-certificate-errors']};
    const qualweb = new QualWeb(plugins);
    await qualweb.start(clusterOptions, launchOptions);

    try {
        // Fetch website details from database based on websiteId
        const website = await Website.findOne({ _id: websiteId });

        if (!website) {
            return res.status(404).json({ error: 'Website not found' });
        }

        // Update website status to 'Em avaliação'
        website.status = 'Em avaliação';
        await website.save();

        console.log(`Evaluating ${pages.length} pages:`, pages);

        // Initiate accessibility evaluation for each page using QualWeb core
        const evaluationResults = [];
        for (const pageObject of pages) {
            // Dentro do loop for na função evaluateWebsiteAccessibility
            const report = await evaluatePage(qualweb, website, pageObject);
            evaluationResults.push(report);
        }

        // Check if any page has status 'Erro na avaliação'
        if (evaluationResults.some(result => result && result.error)) {            website.status = 'Erro na avaliação';
            // Update the status of the pages that had an error during evaluation
            website.pages.forEach(page => {
                if (evaluationResults.some(result => result.error && result.error.includes(page.url))) {
                    page.status = 'Erro na avaliação';
                }
            });
        } else {
            website.status = 'Avaliado';
        }

        // Save the website document with the updated pages and status
        await website.save();

        // Stop QualWeb
        await qualweb.stop();
        res.status(200).json(website);
        
    } catch (error) {
        console.error('Error evaluating accessibility:', error);
        res.status(500).json({ error: 'Internal server error' });
    }
};

    async function evaluatePage(qualweb, website, pageObject) {
        //variaveis para as estatisticas
        let errorCodes = [];
        let hasErrorsA = false;
        let hasErrorsAA = false;
        let hasErrorsAAA = false;

        let passedTests = 0;
        let warningTests = 0;
        let failedTests = 0;
        let inapplicableTests = 0;
        let actRulesTestsResults = [];
        let wcagTestsResults = [];

        const page = website.pages.find(page => page.url === pageObject.url);


        //console.log(`Evaluating page: ${pageObject.url}`);

        if (!page) {
            console.log(`Page not found: ${pageObject.url}`);
            return { error: `Page not found: ${pageObject.url}` };
        }

        if (page.status === 'Avaliado') {
            console.log(`Page already evaluated: ${page.url}`);
            return { alreadyEvaluated: true };
        }

        // Execute accessibility evaluation
        const qualwebOptions = { url: page.url };
        let report;
        try {
            report = await qualweb.evaluate(qualwebOptions);
            //console.log(`Evaluation result for ${pageObject.url}:`, report);
        } catch (error) {
            console.error('Error during QualWeb evaluation:', error);
            throw error;
        }

        // Extract the errors from the evaluation
        let errors = [];
        //console.log("ver a pagina ", page.url);
        const pageReport = report[page.url];
        if (pageReport === undefined) {
            console.log("Não foi possivel avaliar a página ", page.url);
        }
        else {
            const modules = pageReport.modules;
            for (const moduleName of Object.keys(modules)) {
                // Skip the 'best-practices' module
                if (moduleName === 'best-practices') {
                    console.log('Skipping best-practices module');
                    continue;
                }

                const module = report[page.url].modules[moduleName];
                //console.log("Ver os erros ", module.metadata.failed);

                passedTests += module.metadata.passed;
                warningTests += module.metadata.warning;
                failedTests += module.metadata.failed;
                inapplicableTests += module.metadata.inapplicable;
                

                // Check if module.assertions is iterable
                if (module.assertions && typeof module.assertions === 'object') {
                    // Iterate over the properties of module.assertions
                    for (const assertionName of Object.keys(module.assertions)) {
                        const assertion = module.assertions[assertionName];
                        let levels = [];

                        if (assertion.metadata.outcome === 'failed') {

                            errors.push(assertion);
                            errorCodes.push(assertion.code);

                            for (let criteria of assertion.metadata['success-criteria']) {
                                let level = criteria.level;
                                if (level === 'A') {
                                    hasErrorsA = true;
                                } else if (level === 'AA') {
                                    hasErrorsAA = true;
                                } else if (level === 'AAA') {
                                    hasErrorsAAA = true;
                                }
                            }
                        }

                        for (let criteria of assertion.metadata['success-criteria']) {
                            let level = criteria.level;
                            if (level === 'A') {
                                levels.push('A');
                                hasErrorsA = true;
                            } else if (level === 'AA') {
                                levels.push('AA');
                                hasErrorsAA = true;
                            } else if (level === 'AAA') {
                                levels.push('AAA');
                                hasErrorsAAA = true;
                            }
                        }

                        const qualwebInfo = {
                            name: assertion.name,
                            code: assertion.code,
                            description: assertion.description,
                            outcome: assertion.metadata.outcome,
                            successCriteria: levels,
                          };

                        if(moduleName === 'act-rules'){
                            actRulesTestsResults.push(qualwebInfo);
                        }
                        if(moduleName === 'wcag-techniques'){
                            wcagTestsResults.push(qualwebInfo);
                        }

                    }
                } else {
                    console.log(`module.assertions is not iterable for module ${moduleName}`);
                }
            }
        }

        // Create a new Statistics object for the page
        const statistics = new Statistics({
            idWebsite: website._id,
            idPage: page._id,
            hasErros: errors.length > 0,
            hasErrosA: hasErrorsA,
            hasErrosAA: hasErrorsAA,
            hasErrosAAA: hasErrorsAAA,
            errorList: errorCodes,
        });

        const detailStatistics = new DetailStatistics({
            idWebsite: website._id,
            idPage: page._id,
            totalPassedTests: passedTests,
            totalWarningTests: warningTests,
            totalFailedTests: failedTests,
            totalInapplicableTests: inapplicableTests,
            actRulesTestsResults: actRulesTestsResults,
            wcagTestsResults: wcagTestsResults,
        });

        // Save the Statistics object to the database
        try {
            await statistics.save();
            console.log('Statistics saved successfully');
            await detailStatistics.save();
            console.log('DetailStatistics saved successfully');
        } catch (err) {
            console.error('Error saving statistics:', err);
        }

        // Check if report is null
        if (report === null) {
            console.log('Report is null');
        }

        // Save the report as a property of the page object
        const reportId = await reportService.saveReportToDatabase(report);
        page.reportId = reportId;

        //console.log("report ", report);

        page.evaluationResult = report;

        if(!hasErrorsA && !hasErrorsAA){
            page.conformity = 'conforme';
        }else{
            page.conformity = 'não conforme';
        }

        page.status = 'Avaliado';

        const updatedData = {
            reportId: page.reportId,
            evaluationResult: page.evaluationResult,
            conformity: page.conformity,
            status: page.status,
        };
        const updatedPageData = await updatePageData(website._id, page._id, updatedData);
        //console.log('Updated page data:', updatedPageData);

        return report;
    }

    // In your backend
async function updatePageData(websiteId, pageId, updatedData) {
    // Find the website in the database
    const website = await Website.findById(websiteId);

    // Find the specific page in the website's pages
    const page = website.pages.id(pageId);

    // Update the page with the new data
    page.set(updatedData);

    // Save the updated website document
    const updatedWebsite = await website.save();

    // Return the updated page data
    return updatedWebsite.pages.id(pageId);
}