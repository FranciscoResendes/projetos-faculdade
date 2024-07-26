const express = require('express');
const Report = require('../models/report'); // Import the Report model
const fs = require('fs');
const PdfPrinter = require('pdfmake');
const pug = require('pug');
const path = require('path');

exports.saveReportToDatabase = async function (report) {
    const newReport = new Report({ content: report });
    const savedReport = await newReport.save();
    return savedReport._id;
}

exports.generateReport = async (req, res) => {
    let file = {};

    const format = req.query.format;
    const reportData = req.body;

    // Extract the website name or URL from the reportData
    const website = reportData.website;

    // Check if website is a string
    if (typeof website !== 'string') {
        console.error('website is not a string:', website);
        res.status(500).json({ message: 'Invalid website data' });
        return;
    }

    // Use the website name or URL to create a base file name
    const baseFileName = `${website.replace(/[^a-z0-9]/gi, '_').toLowerCase()}_report`;

    // Append the current timestamp to create a unique file name
    const timestamp = new Date().toISOString().replace(/[:.-]/g, '_');
    const fileName = `${baseFileName}_${timestamp}`;

    // Check if the public directory exists and create it if it doesn't
    const publicDir = path.join(__dirname, '..', 'public');
    if (!fs.existsSync(publicDir)) {
        fs.mkdirSync(publicDir);
    }

    try {
        if (format === 'PDF') {
            // Generate PDF report
            console.log('reportData:', reportData)
            const fonts = {
                Roboto: {
                    normal: path.join(__dirname, '..', 'node_modules', '@fontsource', 'roboto', 'files', 'roboto-latin-400-normal.woff'),
                    bold: path.join(__dirname, '..', 'node_modules', '@fontsource', 'roboto', 'files', 'roboto-latin-500-normal.woff'),
                    italics: path.join(__dirname, '..', 'node_modules', '@fontsource', 'roboto', 'files', 'roboto-latin-400-italic.woff'),
                    bolditalics: path.join(__dirname, '..', 'node_modules', '@fontsource', 'roboto', 'files', 'roboto-latin-500-italic.woff')
                }
            };

            const printer = new PdfPrinter(fonts);
            const docDefinition = {
                content: [
                    { text: `Website: ${reportData.website}`, fontSize: 14, bold: true, margin: [0, 0, 0, 10] },
                    { text: `Number of Pages Evaluated: ${reportData.numDePaginasAvaliadas}`, fontSize: 12 },
                    { text: `Number of Pages Without Errors: ${reportData.numDePaginasSemErros}`, fontSize: 12 },
                    { text: `Number of Pages With Errors: ${reportData.numDePaginasComErros}`, fontSize: 12 },
                    { text: `Number of Pages With A-Level Errors: ${reportData.numDePaginasComErrosA}`, fontSize: 12 },
                    { text: `Number of Pages With AA-Level Errors: ${reportData.numDePaginasComErrosAA}`, fontSize: 12 },
                    { text: `Number of Pages With AAA-Level Errors: ${reportData.numDePaginasComErrosAAA}`, fontSize: 12 },
                    { text: `Percentage of Pages Without Errors: ${reportData.percentSemErros}%`, fontSize: 12 },
                    { text: `Percentage of Pages With Errors: ${reportData.percentComErros}%`, fontSize: 12 },
                    { text: `Percentage of Pages With A-Level Errors: ${reportData.percentComErrosA}%`, fontSize: 12 },
                    { text: `Percentage of Pages With AA-Level Errors: ${reportData.percentComErrosAA}%`, fontSize: 12 },
                    { text: `Percentage of Pages With AAA-Level Errors: ${reportData.percentComErrosAAA}%`, fontSize: 12 },
                    {
                        text: 'Top 10 Errors:',
                        fontSize: 14,
                        bold: true,
                        margin: [0, 20, 0, 10]
                    },
                    {
                        ol: reportData.top10Errors.map(error => ({
                            text: `${error[0]}: ${error[1]}`,
                            fontSize: 12
                        }))
                    }
                ]
            };

            const pdfDoc = printer.createPdfKitDocument(docDefinition);
            file.path = path.join(publicDir, `${fileName}.pdf`);
            file.name = `${fileName}.pdf`;

            const writeStream = fs.createWriteStream(file.path);
            pdfDoc.pipe(writeStream);
            pdfDoc.on('end', () => {
                const fileUrl = `${req.protocol}://${req.get('host')}/public/${file.name}`;
                console.log(`File path: ${file.path}`); // Log the file path
                console.log(`File exists: ${fs.existsSync(file.path)}`); // Check if the file exists
                res.json({ fileUrl });
            });
            pdfDoc.end();
        } else if (format === 'HTML') {
            // Define Pug template
            const template = `
doctype html
html
  head
    title Report
  body
    h1 Website: #{website}
    p Number of Pages Evaluated: #{numDePaginasAvaliadas}
    p Number of Pages Without Errors: #{numDePaginasSemErros}
    p Number of Pages With Errors: #{numDePaginasComErros}
    p Number of Pages With A-Level Errors: #{numDePaginasComErrosA}
    p Number of Pages With AA-Level Errors: #{numDePaginasComErrosAA}
    p Number of Pages With AAA-Level Errors: #{numDePaginasComErrosAAA}
    p Percentage of Pages Without Errors: #{percentSemErros}%
    p Percentage of Pages With Errors: #{percentComErros}%
    p Percentage of Pages With A-Level Errors: #{percentComErrosA}%
    p Percentage of Pages With AA-Level Errors: #{percentComErrosAA}%
    p Percentage of Pages With AAA-Level Errors: #{percentComErrosAAA}%
    h2 Top 10 Errors
    ol
      each error in top10Errors
        li #{error[0]}: #{error[1]}
`;

            // Generate HTML report
            const html = pug.render(template, reportData, { pretty: true });
            file.path = path.join(publicDir, `${fileName}.html`);
            file.name = `${fileName}.html`;

            fs.writeFileSync(file.path, html);
            const fileUrl = `${req.protocol}://${req.get('host')}/public/${file.name}`;
            console.log(`File path: ${file.path}`); // Log the file path
            console.log(`File exists: ${fs.existsSync(file.path)}`); // Check if the file exists
            res.json({ fileUrl });
        }
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: err.message });
    }
};
