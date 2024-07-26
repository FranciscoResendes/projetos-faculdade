import { Component } from '@angular/core';
import { WebsiteService } from '../website.service';
import { Location } from '@angular/common';
import { IWebsite } from '../website';
import { IPage } from '../page';


@Component({
  selector: 'app-add-page',
  templateUrl: './add-page.component.html',
  styleUrls: ['./add-page.component.css']
})
export class AddPageComponent {

  pageUrl: string = '';
  pageUrls: string = '';
  currentWebsite: IWebsite | null = null;
  pageInvalid: boolean = false;

  ngOnInit() {
    this.websiteService.getCurrentWebsite().subscribe((website: IWebsite | null) => {
      this.currentWebsite = website;
      console.log("AddPageComponent ngOnInit " + this.currentWebsite?.url);
    });
  }

  constructor(private location: Location, public websiteService: WebsiteService) {
    this.websiteService.getCurrentWebsite().subscribe((website: IWebsite | null) => {
      this.currentWebsite = website;
    });
  }

// Em AddPageComponent
addPage(pageUrl: string) {
  if (this.currentWebsite && !pageUrl.startsWith(this.currentWebsite.url)) {
    this.pageInvalid = true;
    console.log("O URL da página não pertence ao site.");
    return;
  }
  const page = { url: pageUrl, appraisalDate: new Date() } as IPage;
  this.websiteService.addPageToWebsite(page).subscribe(updatedWebsite => {
    console.log('Website updated', updatedWebsite);
    this.pageUrl = '';
    this.pageInvalid = false;
  });
}

  goBack(): void {
    this.location.back();
  }

/* 
  addPages() {
    try {
      const pages = this.pageUrls.split(',').map(url => url.trim());
      this.websiteService.addPagesToWebsite(this.currentWebsite, pages).subscribe(() => {
        this.pageUrls = '';
      });
    } catch {
      console.log("error")
    }
  } */
}
