import { Component, Input, OnInit } from '@angular/core';
import { ActionType } from 'src/app/models/action-type';
import { MuseumService } from 'src/app/museum.service';

@Component({
  selector: 'app-action-type',
  templateUrl: './action-type.component.html',
  styleUrls: ['./action-type.component.css']
})
export class ActionTypeComponent implements OnInit {

  @Input() ActionType : ActionType;
  @Input() name : string;
  preEditActionType : ActionType;
  editing : boolean = true;

  constructor(private service: MuseumService) { }

  ngOnInit(): void {
    this.name = this.ActionType.actionType.replace(/\s/g,"").toLowerCase();
  }

  toggleEdit(){
    this.editing=!this.editing;
    this.preEditActionType = {...this.ActionType}
  }

  editActionType(){
    this.editing=!this.editing;
    return this.service.editActionType(this.ActionType,this.ActionType.actionTypeId).subscribe(x => console.log(x))
  }

  onCancel(){
    this.editing = !this.editing;
    this.ActionType.actionType = this.preEditActionType.actionType;
    this.ActionType.actionTypeDescription = this.preEditActionType.actionTypeDescription;
    this.ActionType.actionTypeId = this.preEditActionType.actionTypeId;
    this.ActionType.actionTypeUrl = this.ActionType.actionTypeUrl;
  }

  removeActionType(){
    if(confirm("Are you sure you would like to delete the entry?")){
    this.editing=!this.editing;
    this.service.removeActionType(this.ActionType.actionTypeId).subscribe(x => console.log(x))
    window.location.reload()
    }
  }

}
