import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Observable} from 'rxjs/Rx';
import {ClientComponent } from '../../components/client/client.component';

@Injectable()
export class ClientService {
    constructor ( private http: HttpClient) {}
    private url = "http://localhost:8080/client";
  
    public getAll():Observable<ClientComponent[]>{
        return this.http.get<ClientComponent[]>(this.url);
    }
     
    public create(client: ClientComponent) : Observable<ClientComponent>{
        return this.http.post<ClientComponent>(this.url, client);
        
    }
    
    public deleteClient(client: ClientComponent): Observable<ClientComponent> {
        return this.http.delete<ClientComponent>(`${this.url}/${client.id}`);
    }
    
    public updateClient(client: ClientComponent): Observable<ClientComponent> {
        return this.http.put<ClientComponent>(`${this.url}/${client.id}`, client);
   }
   public searchClient(id:number):Observable<ClientComponent> {
      return this.http.get<ClientComponent>(`${this.url}/${id}`);
   }
   onSubmit({value,valid}:{value:ClientComponent, valid:boolean}){
       console.log(value);
   }
 }



