import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WebsiteService } from '../website.service';
import { IWebsite } from '../website';
import { IPage } from '../page';

@Component({
  selector: 'app-detail-statistics',
  templateUrl: './detail-statistics.component.html',
  styleUrls: ['./detail-statistics.component.css']
})
export class DetailStatisticsComponent {
  cwagFiltered: boolean = true;
  actFiltered: boolean = true;

  levelaaa: boolean = true;
  levelaa: boolean = true;
  levela: boolean = true;

  passed: boolean = true;
  failed: boolean = true;
  warning: boolean = true;
  inapplicable: boolean = false;

  totalPassedTests: number = 0;
  totalWarningTests: number = 0;
  totalFailedTests: number = 0;
  totalInapplicableTest: number = 0;

  passedTestsPercentage: number = 0;
warningTestsPercentage: number = 0;
failedTestsPercentage: number = 0;
inapplicableTestsPercentage: number = 0;

  actRulesTestsResults: any[] = [];
  wcagTestsResults: any[] = [];

  filterList: any[] = []

  website = {} as IWebsite;
  page = {} as IPage;

  filterTests(s: string) {
    if(s === 'cwagFiltered'){
      this.cwagFiltered = !this.cwagFiltered;
    }
    else if (s === 'actFiltered'){
      this.actFiltered = !this.actFiltered;
    } 
    else if(s === 'passed'){
      this.passed = !this.passed;
    }
    else if(s === 'failed'){
      this.failed = !this.failed;
    }
    else if(s === 'warning'){
      this.warning = !this.warning;
    }
    else if(s === 'not applicable'){
      this.inapplicable = !this.inapplicable;
    }
    else if(s === 'levela'){
      this.levela = !this.levela;
    }
    else if(s === 'levelaa'){
      this.levelaa = !this.levelaa;
    }
    else if(s === 'levelaaa'){
      this.levelaaa = !this.levelaaa;
    }
  }
  filterRest(list: any[]){
    let results: any[] = [];
  
    for(let res of list){
      let shouldAdd = false;
  
      switch(res.outcome) {
        case "passed":
          shouldAdd = this.passed;
          break;
        case "failed":
          shouldAdd = this.failed;
          break;
        case "warning":
          shouldAdd = this.warning;
          break;
        case "inapplicable":
          shouldAdd = this.inapplicable;
          break;
      }
  
      if (res.successCriteria.includes("A")) {
        shouldAdd = shouldAdd && this.levela;
      } else if (res.successCriteria.includes("AA")) {
        shouldAdd = shouldAdd && this.levelaa;
      } else if (res.successCriteria.includes("AAA")) {
        shouldAdd = shouldAdd && this.levelaaa;
      }
  
      if (shouldAdd) {
        results.push(res);
      }
    }
  
    return results;
  }

constructor(private route: ActivatedRoute, private websiteService: WebsiteService) {}

 ngOnInit(): void {
  const websiteId = this.route.snapshot.paramMap.get('websiteId')!;
  const pageId = this.route.snapshot.paramMap.get('pageId')!;

  this.websiteService.getWebsitePage(websiteId, pageId).subscribe((data: any |null) => {
    this.page = data;
    console.log(this.page);
  });
  
  this.websiteService.getDetailStatistics(websiteId, pageId).subscribe((data: any |null) => {
    if(data && data.length > 0){
      console.log(data[0]);
    }
    this.totalPassedTests = data[0].totalPassedTests;
    this.totalWarningTests = data[0].totalWarningTests;
    this.totalFailedTests = data[0].totalFailedTests;
    this.totalInapplicableTest = data[0].totalInapplicableTests;
    this.actRulesTestsResults = data[0].actRulesTestsResults;
    this.wcagTestsResults = data[0].wcagTestsResults;

    let totalTests = this.totalPassedTests + this.totalWarningTests + this.totalFailedTests + this.totalInapplicableTest;
  
    this.passedTestsPercentage = (this.totalPassedTests / totalTests) * 100;
    this.warningTestsPercentage = (this.totalWarningTests / totalTests) * 100;
    this.failedTestsPercentage = (this.totalFailedTests / totalTests) * 100;
    this.inapplicableTestsPercentage = (this.totalInapplicableTest / totalTests) * 100;
  });
 }

}


