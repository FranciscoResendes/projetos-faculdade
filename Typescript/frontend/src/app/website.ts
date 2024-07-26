import { IPage } from './page';

export interface IWebsite {
    _id: number;
    url: string;
    pages: IPage[]; 
    status: string;
    submissionDate: Date;
    appraisalDate: Date;
}