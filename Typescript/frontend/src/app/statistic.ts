export interface Statistic {
    _id: string;
    idWebsite: string;
    idPage: string;
    hasErros: boolean;
    hasErrosA: boolean;
    hasErrosAA: boolean;
    hasErrosAAA: boolean;
    errorList: string[];
    __v: number;
  }