package ai.ilikeplaces.util;

import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.PrivateLocation;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.validators.unit.GeoCoord;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.logic.validators.unit.SimpleString;
import ai.ilikeplaces.logic.validators.unit.VDouble;

public class UserIntroduction {
    public static void createIntroData(HumanId newUser) {

        SettingMoment:
        {
            GeoCoord geoCoord = new GeoCoord().setObj("27.17311262887182", "78.04209488227843");
            final Return<PrivateLocation> privateLocationReturn = DB.getHumanCrudPrivateLocationLocal(false).cPrivateLocation(
                    newUser,
                    new SimpleString("Taj Mahal"),
                    new SimpleString("The symbol of love"),
                    new VDouble(geoCoord.getObjectAsValid().getLatitude()),
                    new VDouble(geoCoord.getObjectAsValid().getLongitude()));

            final Return<PrivateEvent> privateEventReturn = DB.getHumanCrudPrivateEventLocal(false).cPrivateEvent(
                    newUser.getHumanId(),
                    privateLocationReturn.returnValue().getPrivateLocationId(),
                    "Trotting Taj Mahal",
                    "and feelin' the luv!",
                    "TODO",
                    "TODO");


            DB.getHumanCrudPrivateEventLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<Long>(privateEventReturn.returnValue().getPrivateEventId()),
                    "I can delete this event. With deleting goes all stuff in it!");


            DB.getHumanCrudPrivateEventLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<Long>(privateEventReturn.returnValue().getPrivateEventId()),
                    "The stuff on this moment is private to it's members only.");


            DB.getHumanCrudPrivateEventLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<Long>(privateEventReturn.returnValue().getPrivateEventId()),
                    "I should add my friends so that I can invite them to this moment (visit http://www.ilikeplaces.com/page/_friends)");


            DB.getHumanCrudPrivateEventLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<Long>(privateEventReturn.returnValue().getPrivateEventId()),
                    "I should also try uploading and forwarding a photo to see what happens! (see below)");


            DB.getHumanCrudPrivateEventLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<Long>(privateEventReturn.returnValue().getPrivateEventId()),
                    "I just started a moment at Taj Mahal!");

        }

        SettingPersonalWall:
        {
            DB.getHumanCrudWallLocal(false).addEntryToWall(
                    newUser,
                    newUser,
                    new Obj<HumanId>(newUser),
                    "Just joined Down Town!");//Not using "I" because when seen on sidebar, says something like "John Steven - Just joined Down Town!"
        }

    }
}
