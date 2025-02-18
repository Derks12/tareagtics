package com.example.clase6gtics.controller;

import com.example.clase6gtics.entity.Product;
import com.example.clase6gtics.entity.Shipper;
import com.example.clase6gtics.repository.ShipperRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    final ShipperRepository shipperRepository;

    public ShipperController(ShipperRepository shipperRepository) {
        this.shipperRepository = shipperRepository;
    }

    @GetMapping(value = {"/list", ""})
    public String listarTransportistas(Model model) {

        List<Shipper> lista = shipperRepository.findAll();
        model.addAttribute("shipperList", lista);

        return "shipper/list";
    }

    @GetMapping("/new")
    public String nuevoTransportistaFrm(@ModelAttribute("shipper") Shipper shipper, Model model ) {

        model.addAttribute("shipperList", shipperRepository.findAll());

        return "shipper/newFrm";
    }

    @PostMapping("/save")
    public String guardarNuevoTransportista(RedirectAttributes attr,
                                            Model model,
                                            @ModelAttribute("shipper") @Valid Shipper shipper,
                                            BindingResult bindingResult) {

        if (shipper.getCompanyName().equals("")) {
            model.addAttribute("errorCompany", "El nombre no puede ser vacío");
            return "shipper/newFrm";
        } else {
            if (shipper.getShipperId() == 0) {
                attr.addFlashAttribute("msg", "Usuario creado exitosamente");
            } else {
                attr.addFlashAttribute("msg", "Usuario actualizado exitosamente");
            }
            shipperRepository.save(shipper);
            return "redirect:/shipper/list";
        }
    }

    @GetMapping("/edit")
    public String editarTransportista( @ModelAttribute("shipper") Shipper shipper1,
                                       Model model, @RequestParam("id") int id) {

        Optional<Shipper> optShipper = shipperRepository.findById(id);

        if (optShipper.isPresent()) {
            shipper1 = optShipper.get();
            model.addAttribute("shipper", shipper1);
            return "shipper/newFrm";
        } else {
            return "redirect:/shipper/list";
        }
    }

    @GetMapping("/delete")
    public String borrarTransportista(Model model,
                                      @RequestParam("id") int id,
                                      RedirectAttributes attr) {

        Optional<Shipper> optShipper = shipperRepository.findById(id);

        if (optShipper.isPresent()) {
            shipperRepository.deleteById(id);
            attr.addFlashAttribute("msg", "Transportista borrado exitosamente");
        }
        return "redirect:/shipper/list";

    }

    @PostMapping("/BuscarTransportistas")
    public String buscarTransportista(@RequestParam("searchField") String searchField,
                                      Model model) {

        List<Shipper> listaTransportistas = shipperRepository.buscarTransPorCompName(searchField);
        model.addAttribute("shipperList", listaTransportistas);

        return "shipper/list";
    }


}