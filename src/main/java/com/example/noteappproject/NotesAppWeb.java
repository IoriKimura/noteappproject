package com.example.noteappproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.io.*;
import java.util.*;

@SpringBootApplication
@Controller
@RequestMapping("/notes")
public class NotesAppWeb {
    private static final String FILE_NAME = "notes.txt";
    private static List<String> notes = new ArrayList<>();
    
    public static void main(String[] args) {
        loadNotes();
        SpringApplication.run(NotesAppWeb.class, args);
    }
    
    @GetMapping
    public String getNotes(Model model) {
        model.addAttribute("notes", notes);
        return "notes";
    }
    
    @PostMapping
    public String addNote(@RequestParam String note) {
        notes.add(note);
        saveNotes();
        return "redirect:/notes";
    }
    
    @PostMapping("/delete")
    public String deleteNote(@RequestParam int index) {
        if (index >= 0 && index < notes.size()) {
            notes.remove(index);
            saveNotes();
        }
        return "redirect:/notes";
    }
    
    private static void loadNotes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                notes.add(line);
            }
        } catch (IOException e) {
            System.out.println("Файл заметок отсутствует или не может быть загружен.");
        }
    }
    
    private static void saveNotes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String note : notes) {
                writer.write(note);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении заметок.");
        }
    }
}
