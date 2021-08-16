package main;

import com.google.gson.Gson;
import model.Data;
import model.Kategorija;
import model.Proizvod;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import static spark.Spark.*;

public class Launcher {
    public static void main(String[] args) {
        staticFiles.location("/public");
        port(5000);
        String pathP = "proizvodi.json";
        String pathK = "kategorije.json";
        HashMap<String, Object> polja = new HashMap<>();
        ArrayList<Proizvod> proizvodi = new ArrayList<Proizvod>();
        ArrayList<Kategorija> kategorije = new ArrayList<Kategorija>();

        get("/", (request, response) -> {   //poziv index.hbs i ispis proizvoda
            polja.put("proizvodi", Data.readFromJsonP(pathP));
            return new ModelAndView(polja,"index.hbs");
        },new HandlebarsTemplateEngine());

        get("/adminStrana",(request, response) -> {  //poziv admin strane i provera za postojecu sesiju
            if(request.session().attribute("user")==null){
                response.redirect("/loginF");
                return null;
            }
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("userName",request.session().attribute("user").toString());
            return new ModelAndView(hashMap,"adminStrana.hbs");
        },new HandlebarsTemplateEngine());

        get("/loginF", (request, response) -> {  //poziv login forme
            return new ModelAndView(null,"loginF.hbs");
        },new HandlebarsTemplateEngine());

        post("/login", (request, response) -> {   //logika za login sesije
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            if(username.equals("admin") && password.equals("admin")){
                request.session().attribute("user","admin");
                response.redirect("/adminStrana");
                return null;
            }
            HashMap<String,Object> hashMap = new HashMap<>();
            polja.put("msg","Pogresan username ili password");
            return new ModelAndView(polja,"loginF.hbs");
        }, new HandlebarsTemplateEngine());

        get("/logout",(request, response) -> { //logika za ponistavanje sesije
            request.session().removeAttribute("user");
            response.redirect("/");
            return null;
        },new HandlebarsTemplateEngine());

        get("/proizvodiStrana", (request, response) -> { //poziv proizvodi strane
            if(request.session().attribute("user")==null){
                response.redirect("/loginF");
                return null;
            }
            polja.put("userName",request.session().attribute("user").toString());

            polja.put("proizvodi", Data.readFromJsonP(pathP));
            return new ModelAndView(polja,"proizvodiStrana.hbs");
        }, new HandlebarsTemplateEngine());

        get("/dodajP", (request, response) -> {   //poziv forme za dodavanje proizvoda
            if(request.session().attribute("user")==null){
                response.redirect("/loginF");
                return null;
            }
            polja.put("userName",request.session().attribute("user").toString());

            polja.put("kategorije", Data.readFromJsonK(pathK));
            return new ModelAndView(polja,"dodajP.hbs");
        }, new HandlebarsTemplateEngine());

        post("/dodajProizvod", (request, response) -> {   //logika za upis proizvoda u proizvodi.json
            String ime = request.queryParams("ime");
            Double cena =Double.parseDouble(request.queryParams("cena"));
            String opis = request.queryParams("opis");
            String trazenaKategorija = request.queryParams("kategorija");
            Object[] kategorija =  Data.readFromJsonK(pathK).stream().filter(c -> c.getIme().equals(trazenaKategorija)).toArray();

            proizvodi.add(new Proizvod(ime,cena,opis,(Kategorija) kategorija[0]));
            Data.writeToJSONP(proizvodi,pathP);
            polja.put("kategorije", Data.readFromJsonK(pathK));
            polja.put("msgDP","Uspesno dodat proizvod");
            return new ModelAndView(polja, "dodajP.hbs");
        }, new HandlebarsTemplateEngine());

        get("/izmeniP/:id", (request, response) -> {  //poziv forme za izmenu proizvoda putem id-a
            if(request.session().attribute("user")==null){
                response.redirect("/loginF");
                return null;
            }
            polja.put("userName",request.session().attribute("user").toString());

            int id = Integer.parseInt(request.params("id"));
            for(Proizvod p:proizvodi) {
                if(p.getId()==id)
                    polja.put("proizvod",p);
            }
            return new ModelAndView(polja,"izmeniP.hbs");
        }, new HandlebarsTemplateEngine());

        post("snimiIzmeneP", (request, response) -> {  //izmena podataka proizvoda
            int id = Integer.parseInt(request.queryParams("id"));
            String trazenaKategorija = request.queryParams("kategorija");
            Object[] kategorija =  Data.readFromJsonK(pathK).stream().filter(c -> c.getIme().equals(trazenaKategorija)).toArray();
            for(Proizvod p:proizvodi) {
                if(p.getId()==id) {
                    p.setIme(request.queryParams("ime"));
                    p.setCena(Double.parseDouble(request.queryParams("cena")));
                    p.setOpis(request.queryParams("opis"));
                    p.setKategorija((Kategorija) kategorija[0]);
                    Data.writeToJSONP(proizvodi,pathP);
                }
            }
            polja.put("msgIP", "Uspesno izmenjen proizvod");
            return new ModelAndView(polja,"izmeniP.hbs");
        }, new HandlebarsTemplateEngine());

        get("/obrisiP/:id", (request, response) -> {  //brisanje proizvoda
            int id = Integer.parseInt(request.params("id"));
            for(Proizvod p:proizvodi) {
                if(p.getId()==id) {
                    proizvodi.remove(p);
                    break;
                }
            }
            Data.writeToJSONP(proizvodi,pathP);
            polja.put("proizvodi", Data.readFromJsonP(pathP));
            return new ModelAndView(polja,"proizvodiStrana.hbs");
        },new HandlebarsTemplateEngine());

        get("/kategorijeStrana",(request, response) -> {  //poziv kategorije strane
            if(request.session().attribute("user")==null){
                response.redirect("/loginF");
                return null;
            }
            polja.put("userName",request.session().attribute("user").toString());

            polja.put("kategorije", Data.readFromJsonK(pathK));
            return new ModelAndView(polja,"kategorijeStrana.hbs");
        }, new HandlebarsTemplateEngine());

        get("/dodajK", (request, response) -> {  //poziv forme za dodavanje kategorija
            if(request.session().attribute("user")==null){
                response.redirect("/loginF");
                return null;
            }
            polja.put("userName",request.session().attribute("user").toString());

            return new ModelAndView(polja,"dodajK.hbs");
        }, new HandlebarsTemplateEngine());

        post("/dodajKategoriju", (request, response) -> {  //logika za upis kategorije u kategorije.json
            String ime = request.queryParams("ime");
            kategorije.add(new Kategorija(ime));
            Data.writeToJSONK(kategorije,pathK);
            polja.put("msgDK","Uspesno dodata kategorija");
            return new ModelAndView(polja,"dodajK.hbs");
        }, new HandlebarsTemplateEngine());

        get("/izmeniK/:id", (request, response) -> {  //poziv forme za izmenu kategorije putem id-a
            if(request.session().attribute("user")==null){
                response.redirect("/loginF");
                return null;
            }
            polja.put("userName",request.session().attribute("user").toString());

            int id = Integer.parseInt(request.params("id"));
            for(Kategorija k:kategorije){
                if(k.getId()==id)
                    polja.put("kategorija",k);
            }
            return new ModelAndView(polja,"izmeniK.hbs");
        }, new HandlebarsTemplateEngine());


        post("/snimiIzmeneK", (request, response) -> {  //izmena podataka kategorije (i na proizvodima)
            int id =Integer.parseInt(request.queryParams("id"));
            for(Kategorija k:kategorije){
                if(k.getId()==id) {
                    k.setIme(request.queryParams("ime"));
                    Data.writeToJSONK(kategorije,pathK);
                    for(Proizvod p:proizvodi) {
                        if(p.getKategorija().getId() == id) {
                            p.getKategorija().setIme(request.queryParams("ime"));
                            Data.writeToJSONP(proizvodi,pathP);
                        }
                    }
                }
            }
            polja.put("msgIK","Uspesno izmenjena kategorija");
            return new ModelAndView(polja,"izmeniK.hbs");
        },new HandlebarsTemplateEngine());

        get("/obrisiK/:id", (request, response) -> {  //brisanje kategorije (provera da li neki proizvod sadrzi kategoriju)
            int id = Integer.parseInt(request.params("id"));
            boolean f = false;
            for(Proizvod p:proizvodi)
                if(p.getKategorija().getId()==id)
                    f = true;
            for(Kategorija k:kategorije) {
                if(k.getId()==id && f == false) {
                    kategorije.remove(k);
                    polja.remove("msgOK");
                    Data.writeToJSONK(kategorije,pathK);
                    break;
                }
                else
                    polja.put("msgOK","Ova kategorija sadrzi proizvod");
            }
            polja.put("kategorije", Data.readFromJsonK(pathK));
            return new ModelAndView(polja,"kategorijeStrana.hbs");
        },new HandlebarsTemplateEngine());

        post("/pretrazi", (request, response) -> { //metoda za pretragu proizvoda
            String pretraga = request.queryParams("pretraga");
            ArrayList<Proizvod> pretProiz = new ArrayList<>();
            HashMap pretrazi = new HashMap();
            for(Proizvod p:proizvodi) {
                if(p.getIme().toLowerCase(Locale.ROOT).contains(pretraga.toLowerCase(Locale.ROOT)) || p.getOpis().toLowerCase(Locale.ROOT).contains(pretraga.toLowerCase(Locale.ROOT)))
                    pretProiz.add(p);
            }
            pretrazi.put("proizvodi", pretProiz);

            return new ModelAndView(pretrazi,"index.hbs");
        }, new HandlebarsTemplateEngine());


    }
}
