import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Http, Response} from '@angular/http';
import {Observable } from "rxjs/Rx";
import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import {ClientComponent } from './components/client/client.component';

import {ClientService } from './services/client/client.service';
import {PagerService } from './services/client/pager.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {  
    constructor(private clientService:ClientService, private pagerService:PagerService){}
    private baseUrl:string = 'http://localhost:8080';
    reactiveForm: FormGroup;
    public submitted: boolean;
    public clients:any[] = [];
    public client: ClientComponent;
    clientForm:boolean = false;
    isNewForm:boolean;
    newClient:any = {}; 
    // pager object
    pager: any = {};

    // paged items
    pagedItems: any[];                   
    
    ngOnInit() {
        this.reactiveForm = new FormGroup({
            name: new FormControl(''),
            address: new FormControl(''),
            date: new FormControl('')  
        });
        //calling service on page load
        this.clientService.getAll()
        .subscribe(

             (clients:any) => {
                this.clients = clients.content;
                // initialize to page 1
                this.setPage(1);
                console.log(clients);   

             },
              
             err => {
                console.log(err);    
            }   
         );          
        
    }
     setPage(page: number) {
        if (page < 1 || page > this.pager.totalPages) {
            return;
        }

        // get pager object from service
        this.pager = this.pagerService.getPager(this.clients.length, page);

        // get current page of items
        this.pagedItems = this.clients.slice(this.pager.startIndex, this.pager.endIndex + 1);
    }    
    removeClient(client:ClientComponent){
        this.clientService.deleteClient(client)
        .subscribe(
          client => {
              this.client = client;
              console.log("successfully deleted");
          },
          err => {
                console.log("error occured");    
          } 
        );   
    }
    
    showEditClientForm(client:ClientComponent){
      if(!client){
        this.clientForm = false;    
        return;
      }  
      this.clientForm = true;
      this.isNewForm = false;
      this.newClient = client;
    }
    showAddClientForm(){
        if(this.clients.length){
            this.newClient = {};
         }
         this.clientForm = true;
         this.isNewForm = true;
    }
    
    saveClient(client:ClientComponent){
        if (this.isNewForm){
            //add new client
           console.log(client);
           client.id = null;
           this.clientService.create(client).
            subscribe(
                res => {
                        console.log("The posted object is successful");
                       },
                err => {
                        console.log("error occured");
                }
            );
        }  
        else {
            //edit the existing client
            this.clientService.updateClient(client)
           .subscribe(
              client => {
                  this.client = client;
                  console.log(client);
              },
              err => {
                    console.log("error occured");    
              } 
               
            ); 
        } 
       this.clientForm = false;
    }
    
    cancelEdits(){
        this.newClient = {};   
    }
}

