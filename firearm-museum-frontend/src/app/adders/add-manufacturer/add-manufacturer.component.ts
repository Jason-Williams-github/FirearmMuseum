import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Manufacturer } from 'src/app/models/manufacturer';
import { MuseumService } from 'src/app/museum.service';


@Component({
  selector: 'app-add-manufacturer',
  templateUrl: './add-manufacturer.component.html',
  styleUrls: ['./add-manufacturer.component.css']
})
export class AddManufacturerComponent implements OnInit {

  imgSrc : string = "https://i.imgur.com/FK3SITD.png";

  constructor(private service: MuseumService, private router : Router) { }

  ngOnInit(): void {
  }

  addManufacturer(name: string, description: string, imgSrc){

    if(name===""){
      alert("All fields are required")
      return;
    }
    if(description===""){
      alert("All fields are required")
      return;
    }
    if(imgSrc===""){
      alert("All fields are required")
      return;
    }

    let x = 5;
    let toAdd : Manufacturer={manufacturer : name, manufacturerDescription : description, manufacturerUrl: imgSrc};
    this.service.addManufacturer(toAdd).subscribe(x => console.log(x));

    this.router.navigateByUrl("/manufacturer");

  }

}
