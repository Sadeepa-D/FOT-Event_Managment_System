package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.location;
import com.example.FOT_Event_Managment_System.Service.AdminlocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminlocationController {

    @Autowired
    private AdminlocationService locationService;

    // View location page
    @GetMapping("/admin/locations")
    public String viewLocations(Model model) {
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("location", new location());
        return "Admin/admin-location";
    }

    // Show Add Location form
    @GetMapping("/admin/locations/add")
    public String showAddLocationForm(Model model) {
        model.addAttribute("location", new location());
        return "Admin/add-location";
    }

    // Save location
    @PostMapping("/admin/locations/save")
    public String saveLocation(@ModelAttribute("location") location loc) {
        locationService.saveLocation(loc);
        return "redirect:/admin/locations";
    }

    // Edit location
    @GetMapping("/admin/locations/edit/{id}")
    public String editLocation(@PathVariable Long id, Model model) {
        model.addAttribute("location", locationService.getLocationById(id));
        model.addAttribute("locations", locationService.getAllLocations());
        return "Admin/add-location";
    }

    // Delete location
    @GetMapping("/admin/locations/delete/{id}")
    public String deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return "redirect:/admin/locations";
    }

    //set availble or not
    @GetMapping("/admin/locations/toggle/{id}")
    public String toggleAvailability(@PathVariable Long id) {
        location loc = locationService.getLocationById(id);
        if(loc != null) {
            loc.setAvailability(loc.getAvailability().equals("available") ? "unavailable" : "available");
            locationService.saveLocation(loc);
        }
        return "redirect:/admin/locations";
    }
}