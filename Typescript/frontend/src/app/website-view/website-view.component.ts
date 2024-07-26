import { Component, OnInit } from '@angular/core';
import { WebsiteService } from '../website.service';
import { Router } from '@angular/router';
import { IWebsite } from '../website';
import { WebsiteComponent } from '../website/website.component';

@Component({
  selector: 'app-website-view',
  templateUrl: './website-view.component.html',
  styleUrls: ['./website-view.component.css']
})
export class WebsiteViewComponent implements OnInit{
  
  constructor(private router: Router, private websiteService: WebsiteService, private websiteComponent: WebsiteComponent) { }

  websites: IWebsite[] = [];
  submissionOrder: boolean = true;
  appraisalOrder: boolean = true;
  filter: string = 'Nenhum';
  invalidWebsite: boolean = false;
  arrowSub: string = "&uArr;";
  arrowAppr: string = "&uArr;";
  cboxCheck: boolean = false;
  
  ngOnInit() {
    this.websiteService.getWebsites().subscribe((webs: IWebsite[]) => {
      this.websites = webs;
    });
  }

  updateWebsites() {
    this.websiteService.getWebsites().subscribe((webs: IWebsite[]) => {
      this.websites = webs;
    });
  }

  sortSub(ascendingOrder: boolean){

    if(ascendingOrder){
      this.websites.sort((a, b) => new Date(b.submissionDate).getTime() - new Date(a.submissionDate).getTime());
    }
    else{
      this.websites.sort((a, b) => new Date(a.submissionDate).getTime() - new Date(b.submissionDate).getTime());
    }
    this.submissionOrder = !this.submissionOrder;
  }

  sortAppr(ascendingOrder: boolean){
    if(ascendingOrder){
      this.websites.sort((a, b) => new Date(b.appraisalDate).getTime() - new Date(a.appraisalDate).getTime());
    }
    else{
      this.websites.sort((a, b) => new Date(a.appraisalDate).getTime() - new Date(b.appraisalDate).getTime());
    }
    this.appraisalOrder = !this.appraisalOrder;
  }

  filterStatus(filter: any){
    let sites = this.websites;
    
    if(filter === "Por avaliar"){
      sites = sites.filter( (a) => a.status === filter);
    }
    else if(filter === "Em avaliação"){
      sites = sites.filter( (a) => a.status === filter);
    }
    else if(filter === "Avaliado"){
      sites = sites.filter( (a) => a.status === filter);
    }
    else if(filter === "Erro na avaliação"){
      sites = sites.filter( (a) => a.status === filter);
    }
    return sites;
  }  

  activateFilter(s: any){
    this.filter = s.target.value;
  }
  
  goToPage(site: IWebsite){
    this.websiteService.changeCurrentWebsite(site);
    this.router.navigate(['/add-page']);
  }

  goToDetails(website: IWebsite) {
    this.router.navigate(['/website-details', website._id]);
  }

  showTime(time: Date){
    return new Date(time).toUTCString();
  }
  changeArrowSub(arr: string){
    if(arr.startsWith('&u')){
      this.arrowSub = "&dArr;";
    }
    else{
      this.arrowSub = "&uArr;"
    }
  }

  changeArrowAppr(arr: string){
    if(arr.startsWith('&u')){
      this.arrowAppr = "&dArr;";
    }
    else{
      this.arrowAppr = "&uArr;"
    }
  }

  deleteWebsite(website: IWebsite | null){
    if (website !== null){
      this.websiteService.deleteWebsite(website).subscribe((res) => {
        console.log(res);
        this.updateWebsites();
      });
    }
  }

  checkAll(check: boolean){
    let array = document.querySelectorAll('.cbox');
    
    if(this.cboxCheck === false){
      
      for (var i = 0; i < array.length; i++){
        let a = array[i] as HTMLInputElement;
        a.checked = true;
      }
    }
    else{
      for (var i = 0; i < array.length; i++){
        let a = array[i] as HTMLInputElement;
        a.checked = false;
      }
    }
    this.cboxCheck = !this.cboxCheck;
  }
  DeleteAllWebsites(){
    let array = document.querySelectorAll('.rowDel');
    let websitesToDelete = [];
    let counter = 0;
    const textOne = 'You have selected ';
    const textTwo = ' website(s) that contain 1 or more pages. Do you still wish to delete?';

    for (var i = 0; i < array.length; i++){
      let a = array[i].children[0].firstChild as HTMLInputElement;

      if (a.checked === true){
        let text = array[i].children[1].textContent;
        let website = this.getWebsiteFromUrl(text);

        if(website !== null && website.pages.length > 0){
          counter++;
        }
        websitesToDelete.push(website);        
      }
    }

    if(counter === 0 || (counter > 0 && confirm(textOne + counter + textTwo))){
      for (i = 0; i <  websitesToDelete.length; i++){
        this.deleteWebsite(websitesToDelete[i]);
      }
    }
  }

  getWebsiteFromUrl(url: string | null){
    for (var i = 0; url !== null && i < this.websites.length; i++){
      if(url === this.websites[i].url){
        return this.websites[i];
      }
    }
    return null;
  }
}
