import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { take } from 'rxjs/operators';
import { switchMap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { IWebsite } from './website';
import { IPage } from './page';
import { of } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class WebsiteService {

  //private apiUrl = 'http://localhost:3000';
  private apiUrl = 'http://appserver.alunos.di.fc.ul.pt:3071';

  private currentWebsite = new BehaviorSubject<IWebsite | null>(null);
  currentWebsite$ = this.currentWebsite.asObservable();
  private currentWebsiteValue: IWebsite | null = null;

  constructor(private http: HttpClient) {
    this.currentWebsite$.subscribe(website => {
      this.currentWebsiteValue = website;
    });
  }

  //websites functions
  changeCurrentWebsite(website: IWebsite) {
    this.currentWebsite.next(website);
  }

  getCurrentWebsite(): Observable<IWebsite | null> {
    return of(this.currentWebsiteValue);
  }

  updateWebsiteStatus(website: IWebsite, status: string): Observable<IWebsite> {
    return this.http.put<IWebsite>(`${this.apiUrl}/api/websites/${website._id}/status`, { status: status });
  }

  addWebsite(website: IWebsite): Observable<IWebsite>{
    return this.http.post<IWebsite>(`${this.apiUrl}/api/websites`, website);
  }

  getWebsites(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/websites`);
  }

  getWebsite(id: string) {
    return this.http.get(`${this.apiUrl}/api/websites/${id}`);
  }

  deleteWebsite(website: IWebsite): Observable<IWebsite>{
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {_id: website._id},
    };
    return this.http.delete<IWebsite>(`${this.apiUrl}/api/websites`, options);
  }

  //Pages functions
  addPageToWebsite(page: IPage): Observable<IWebsite> {
    return this.currentWebsite$.pipe(
      take(1),
      filter((website: IWebsite | null): website is IWebsite => website !== null),
      switchMap((website: IWebsite) => {
        website.pages.push(page);
        return this.http.put<IWebsite>(`${this.apiUrl}/api/websites/${website._id}`, { page });
      })
    );
  }

  getPages(id: string) {
    return this.http.get(`${this.apiUrl}/api/websites/${id}/pages`);
  }

  getWebsitePages(id: string) {
    return this.http.get(`${this.apiUrl}/api/websites/${id}/pages`);
  }

  getWebsitePage(id: string, pageId: string) {
    return this.http.get(`${this.apiUrl}/api/websites/${id}/${pageId}`);
  }

  deletePages(pages: IPage[], website: IWebsite): Observable<IPage>{
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {pages: pages, webId: website._id},
    };
    return this.http.delete<IPage>(`${this.apiUrl}/api/websites/pages`, options);
  }

  //evaluation functions
  evaluatePages(website: IWebsite, pages: IPage[]): Observable<IWebsite | null> {
    return this.http.patch<IWebsite>(`${this.apiUrl}/api/evaluate`, { website, pages });
  }

  getStatistics(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/statistics/${id}`);
  }

  getDetailStatistics(websiteId: string, pageId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/detailedStatistics/${websiteId}/${pageId}`);
  }

  exportReport(reportData: any, format: string) {
    return this.http.post<string>(`${this.apiUrl}/api/reports/export?format=${format}`, reportData);
  }

}
  