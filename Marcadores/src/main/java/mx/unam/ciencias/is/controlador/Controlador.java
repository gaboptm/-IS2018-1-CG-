/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.unam.ciencias.is.controlador;

import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mx.unam.ciencias.is.mapeobd.Marcador;
import mx.unam.ciencias.is.modelo.MarcadorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author jonathan
 */
@Controller 
public class Controlador {
    /*Injectamos el modelo del marcador */
    @Autowired
    MarcadorDAO marcador_db;
    
    /**
     * Regresa la pagina principal con los marcadores de la base de datos
     * @param model 
     * @return regresa el modelo 
     */
    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView marcadores(ModelMap model){
        List<Marcador> mar = marcador_db.getMarcadores();
          
        model.addAttribute("marcadores", mar);
        
        return new ModelAndView("login",model);
    
    }
    
   
    /**
     * Regresa el nombre de el jsp agrega marcador
     * @return 
     */
    @RequestMapping(value="/agregaMarcador")
    public String agregaMarcador(){
        return "agregaMarcador";
    }
    
    /**
     * Guarda un nuevo marcador 
     * @param request los atributos del marcador.
     * @return 
     */
    @RequestMapping(value="/guardaMarcador", method = RequestMethod.GET)
    public String guardaMarcador(HttpServletRequest request){
        Double latitud = Double.parseDouble(request.getParameter("latitud"));
        Double longitud = Double.parseDouble(request.getParameter("longitud"));
        String nombre = request.getParameter("nombreM");
        String descripcion = request.getParameter("descripcion");
        Marcador ma = marcador_db.getMarcador(latitud, longitud);
        if(ma==null){
            Marcador m  = new Marcador();
            m.setVarLatitud(latitud);
            m.setVarLongitud(longitud);
            m.setVarNombreM(nombre);
            m.setVarDescripcion(descripcion);
            marcador_db.guardar(m);
        
        }
        return "redirect:/";
    }
    
    
    
    /**
     * Actualiza el marcador
     * @param model
     * @param request
     * @return 
     */ 
    @RequestMapping(value="/actualizaM", method = RequestMethod.GET)
    public ModelAndView actualizaM(ModelMap model,HttpServletRequest request){
        Double latitud = Double.parseDouble(request.getParameter("latitud"));
        Double longitud = Double.parseDouble(request.getParameter("longitud"));
         Marcador m = marcador_db.getMarcador(latitud,longitud);
         model.addAttribute("marcador",m);
         return new ModelAndView("actualizaM",model);
    
    }
    
    
    @RequestMapping(value="/eliminaMarcador", method = RequestMethod.GET)
    public String eliminaMarcador(HttpServletRequest request){
         Double latitud = Double.parseDouble(request.getParameter("latitud"));
        Double longitud = Double.parseDouble(request.getParameter("longitud"));
        Marcador m = marcador_db.getMarcador(latitud, longitud);
        if(m!=null){
            marcador_db.eliminar(m);
        }
        return "redirect:/";
    }
    
    @RequestMapping(value= "/actualizar", method = RequestMethod.POST)
    public String actualizar(HttpServletRequest request){
       int id = Integer.parseInt(request.getParameter("id"));
       Double latitud;
       Double longitud;
       String nombre = ""; 
       String descripcion = ""; 
       Marcador ma = marcador_db.getMarcadorId(id);
       if (ma != null){
        if (!request.getParameter("latitud").equals(""))
                latitud = Double.parseDouble(request.getParameter("latitud"));
        else latitud = 0.0;
       
        if (!request.getParameter("longitud").equals(""))
                longitud = Double.parseDouble(request.getParameter("longitud"));
        else longitud = 0.0;
      
        if (!request.getParameter("nombre").equals("")){
            nombre = request.getParameter("nombre");
            ma.setVarNombreM(nombre);
        }else nombre = "nombre";
        
        if (!request.getParameter("desc").equals("")){
            descripcion = request.getParameter("desc");
            ma.setVarDescripcion(descripcion);
        }else descripcion = "descripcion"; 
        
        marcador_db.actualizar(ma);
       }
        return "redirect:/";
 
    }
}
