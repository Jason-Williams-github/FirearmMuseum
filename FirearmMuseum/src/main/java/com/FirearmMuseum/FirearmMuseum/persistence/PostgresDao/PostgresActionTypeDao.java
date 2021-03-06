package com.FirearmMuseum.FirearmMuseum.persistence.PostgresDao;

import com.FirearmMuseum.FirearmMuseum.models.ActionType;
import com.FirearmMuseum.FirearmMuseum.persistence.Dao.ActionTypeDao;
import com.FirearmMuseum.FirearmMuseum.persistence.mappers.idMappers.ActionTypeIdMapper;
import com.FirearmMuseum.FirearmMuseum.persistence.mappers.ActionTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InvalidObjectException;
import java.util.List;

@Component
public class PostgresActionTypeDao implements ActionTypeDao {

    @Autowired
    private JdbcTemplate template;


    @Override
    public List<ActionType> getAllActionTypes() {

        List<ActionType> allActionTypes = template.query("SELECT * FROM \"ActionType\"",
                new ActionTypeMapper());

        return allActionTypes;
    }

    @Override
    public ActionType addActionType(ActionType toAdd) throws InvalidObjectException {
        if(toAdd==null)
            throw new InvalidObjectException("The entered action type was null");
        if(toAdd.getActionType()==null)
            throw new IllegalArgumentException("the entered action type was null");

        try{
            Integer actionTypeId = template.queryForObject("INSERT INTO \"ActionType\" (\"actiontypename\",\"actiontypedescription\",\"actiontypeurl\")" +
                            "VALUES (?,?,?) RETURNING \"actiontypeid\";",
                    new ActionTypeIdMapper(),
                    toAdd.getActionType(),
                    toAdd.getActionTypeDescription(),
                    toAdd.getActionTypeUrl());
                    toAdd.setActionTypeId( actionTypeId );

        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("an invalid Id was entered");
        }

        if(toAdd.getActionTypeId()==null){
            throw new IllegalArgumentException("the action type id was null");
        }
        return toAdd;
    }

    @Override
    public void editActionType(Integer id, ActionType toEdit) {

        boolean idExists = false;
        for (int i = 0; i < getAllActionTypes().size(); i++) {
            if(getAllActionTypes().get(i).getActionTypeId().equals(id)){
                idExists=true;
            }
        }

        if(!idExists)
            throw new IllegalArgumentException("The id entered does not exist");

        String newActionType = toEdit.getActionType().replace("'","''");
        String newActionTypeDescription = toEdit.getActionTypeDescription().replace("'","''");
        String newUrl = toEdit.getActionTypeUrl();
        ActionType original = getActionTypeById(id);

        if(newActionType!=null){
            template.update("UPDATE \"ActionType\" SET \"actiontypename\" = '" + newActionType + "' WHERE \"actiontypeid\" = '" + id + "';");
            original.setActionType(newActionType);
        }
        if(newActionTypeDescription!=null){
            template.update("UPDATE \"ActionType\" SET \"actiontypedescription\" = '" + newActionTypeDescription + "' WHERE \"actiontypeid\" = '"+id+"';");
            original.setActionTypeDescription(newActionTypeDescription);
        }
        if(newUrl!=null){
            template.update("UPDATE \"ActionType\" SET \"actiontypeurl\" = '" + newUrl + "' WHERE \"actiontypeid\" = '"+id+"';");
            original.setActionTypeUrl(newUrl);
        }

    }


    @Override
    public void removeActionTypeById(Integer id) {
        boolean idEXists = false;
        for(int i = 0;i < getAllActionTypes().size() && !idEXists;i++){
            if(getAllActionTypes().get(i).getActionTypeId().equals(id))
                idEXists=true;
        }

        if(idEXists) {
            template.update("DELETE FROM \"ActionType\"" +
                    "WHERE \"ActionType\".actiontypeid = '" + id + "';");
        } else
            throw new IllegalArgumentException("The action type with that id doesn't exist");
    }

    @Override
    public ActionType getActionTypeById(Integer id) {
        ActionType toReturn = null;

        for (int i = 0; i < getAllActionTypes().size(); i++) {
            if(id == getAllActionTypes().get(i).getActionTypeId()){
                toReturn = getAllActionTypes().get(i);
            }
        }
        if(toReturn==null)
            throw new IllegalArgumentException("Action type with id " + id + " does not exist");
        return toReturn;
    }
}
