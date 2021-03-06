package com.FirearmMuseum.FirearmMuseum.persistence.PostgresDao;

import com.FirearmMuseum.FirearmMuseum.models.FirearmType;
import com.FirearmMuseum.FirearmMuseum.persistence.Dao.FirearmTypeDao;
import com.FirearmMuseum.FirearmMuseum.persistence.mappers.idMappers.FirearmTypeIdMapper;
import com.FirearmMuseum.FirearmMuseum.persistence.mappers.FirearmTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InvalidObjectException;
import java.util.List;

@Component
public class PostgresFirearmTypeDao implements FirearmTypeDao {

    @Autowired
    JdbcTemplate template;

    public List<FirearmType> getAllFirearmTypes(){

        List<FirearmType> allFirearmTypes = template.query("SELECT * FROM \"FirearmType\"",
                new FirearmTypeMapper());

        return allFirearmTypes;
    }

    public FirearmType addFirearmType(FirearmType toAdd) throws InvalidObjectException {
        if(toAdd==null)
            throw new InvalidObjectException("The entered firearm type was null");
        if(toAdd.getFirearmType()==null)
            throw new IllegalArgumentException("the entered firearm type was null");

        try{
            Integer FirearmTypeId = template.queryForObject("INSERT INTO \"FirearmType\" (\"firearmtypename\",\"firearmtypedescription\",\"firearmtypeurl\")" +
                            "VALUES (?,?,?) RETURNING \"firearmtypeid\";",
                    new FirearmTypeIdMapper(),
                    toAdd.getFirearmType(),
                    toAdd.getFirearmTypeDescription(),
                    toAdd.getFirearmTypeUrl());
                    toAdd.setFirearmTypeId( FirearmTypeId );

        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("an invalid Id was entered");
        }

        if(toAdd.getFirearmTypeId()==null){
            throw new IllegalArgumentException("the firearm type id was null");
        }
        return toAdd;
    }

    public void editFirearmType(Integer id, FirearmType toEdit){
        boolean idExists = false;
        for (int i = 0; i < getAllFirearmTypes().size(); i++) {
            if(getAllFirearmTypes().get(i).getFirearmTypeId().equals(id)){
                idExists=true;
            }
        }

        if(!idExists)
            throw new IllegalArgumentException("The id entered does not exist");

        String newFirearmType = toEdit.getFirearmType().replace("'","''");
        String newFiearmTypeDescription = toEdit.getFirearmTypeDescription().replace("'","''");
        String newUrl = toEdit.getFirearmTypeUrl();
        FirearmType original = getFirearmTypeById(id);

        if(newFirearmType!=null){
            template.update("UPDATE \"FirearmType\" SET \"firearmtypenmae\" = '" + newFirearmType + "' WHERE \"firearmtypeid\" = '" + id + "';");
            original.setFirearmType(newFirearmType);
        }
        if(newFiearmTypeDescription!=null){
            template.update("UPDATE \"FirearmType\" SET \"firearmtypedescription\" = '" + newFiearmTypeDescription + "' WHERE \"firearmtypeid\" = '" + id + "';");
            original.setFirearmTypeDescription(newFiearmTypeDescription);
        }
        if(newUrl!=null){
            template.update("UPDATE \"FirearmType\" SET \"firearmtypeurl\" = '" + newUrl + "' WHERE \"firearmtypeid\" = '" + id + "';");
            original.setFirearmTypeUrl(newUrl);
        }
    }

    public void removeFirearmTypeById(Integer id){
        boolean idEXists = false;
        for(int i = 0;i < getAllFirearmTypes().size() && !idEXists;i++){
            if(getAllFirearmTypes().get(i).getFirearmTypeId().equals(id))
                idEXists=true;
        }

        if(idEXists) {
            template.update("DELETE FROM \"FirearmType\"" +
                    "WHERE \"firearmtype\".firearmtypeid = '" + id + "';");
        } else
            throw new IllegalArgumentException("The firearm type with that id doesn't exist");
    }

    public FirearmType getFirearmTypeById(Integer id){
        FirearmType toReturn = null;

        for (int i = 0; i < getAllFirearmTypes().size(); i++) {
            if(id == getAllFirearmTypes().get(i).getFirearmTypeId()){
                toReturn = getAllFirearmTypes().get(i);
            }
        }
        if(toReturn==null)
            throw new IllegalArgumentException("Firearm type with id " + id + " does not exist");
        return toReturn;
    }


}
