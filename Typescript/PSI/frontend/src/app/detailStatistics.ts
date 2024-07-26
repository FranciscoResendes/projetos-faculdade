export interface Statistic {
    _id: string;
    idWebsite: string;
    idPage: string;
    totalPassedTests: number;
    totalWarningTests: number;
    totalFailedTests: number;
    totalInapplicableTests: number;
    errorList: string[];
    __v: number;
  }