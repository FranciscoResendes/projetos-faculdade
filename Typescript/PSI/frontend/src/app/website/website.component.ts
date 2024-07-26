import { Component, OnInit } from '@angular/core';
import { WebsiteService } from '../website.service';
import { Router } from '@angular/router';
import { IWebsite } from '../website';

@Component({
  selector: 'app-website',
  templateUrl: './website.component.html',
  styleUrls: ['./website.component.css']
})
export class WebsiteComponent implements OnInit {
  url: string = '';
  websites: IWebsite[] = [];
  invalidWebsite: boolean = false;

  constructor(private router: Router, private websiteService: WebsiteService) { }

  ngOnInit() {
    this.websiteService.getWebsites().subscribe((websites: IWebsite[]) => {
      this.websites = websites;
    });
  }

  addWebsite(url: string) {
    try{
      const urlcheck = new URL(url);
      this.invalidWebsite = false;
      const website = { url: url } as IWebsite;
      this.websiteService.addWebsite(website)
        .subscribe((websiteFromServer: IWebsite) => {
        this.websites.push(websiteFromServer);
        this.websiteService.changeCurrentWebsite(websiteFromServer);
        this.router.navigate(['/add-page']);
      });
      this.url = '';
    } catch{
      this.invalidWebsite = true;
    }


  }

  ackCorrectWebsiteURL() {
    return this.url !== '' && new URL(this.url).hostname !== 'localhost';
  }
}