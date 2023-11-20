import { Component, OnInit } from '@angular/core';
import razerPaymentStatus from '../MyPlugin/RazerStatus';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  displayData: any;

  constructor() {}


  async loadlogs(){
    const response = await razerPaymentStatus.loadLogs();
  
    this.displayData = JSON.stringify(response);
    // this.displayData.push(response);

  }

  ngOnInit() {
    setInterval(async () => {
      await this.loadlogs();
    }, 10);
  }

  

  async oncreate(){
    let updateCredential_value = [{"username":"1AyG5OtZLFp0AjXwiZrP","developer":"KgrDiYb7rfFZLcP"}];
        try {
          const response = await razerPaymentStatus.onCreate({ value: JSON.stringify(updateCredential_value) });
          this.displayData = JSON.stringify(response);
        } catch (error) {
          console.error('Error:', error);
        }
      }
  async refreshtoken(){
    let updateCredential_value = [{"username":"1AyG5OtZLFp0AjXwiZrP","developer":"KgrDiYb7rfFZLcP"}];
            try {
              const response = await razerPaymentStatus.refreshtoken({ value: JSON.stringify(updateCredential_value) });
              this.displayData = JSON.stringify(response);
            } catch (error) {
              console.error('Error:', error);
            }
          }

  async starttransfer(){
            let updateCredential_value = [{"username":"1AyG5OtZLFp0AjXwiZrP","developer":"KgrDiYb7rfFZLcP","amount" : "100"}];
                try {
                  const response = await razerPaymentStatus.starttranstion({ value: JSON.stringify(updateCredential_value) });
                  this.displayData = JSON.stringify(response);
                } catch (error) {
                  console.error('Error:', error);
                }
              }

}
