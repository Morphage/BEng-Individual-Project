/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.practice;

/**
 *
 * @author achantreau
 */
public class CategorySidebarInfo {
    
    private String description;
    private String[] lectureNotes;
    private String[] helpfulLinks;
    
    private String[] nullArray = {"No links provided"};
    
    public CategorySidebarInfo(String description, String lectureNotes,
            String helpfulLinks) {
        if (description == null) {
            this.description = "No description provided";
        } else {
            this.description = description;
        }
        
        if (lectureNotes != null) {
            this.lectureNotes = lectureNotes.split(";");
        } else {
            this.lectureNotes = nullArray;
        }
        
        if (helpfulLinks != null) {
            this.helpfulLinks = helpfulLinks.split(";");
        } else {
            this.helpfulLinks = nullArray;
        }
    }

    public String getDescription() {
        return description;
    }

    public String[] getLectureNotes() {
        return lectureNotes;
    }

    public String[] getHelpfulLinks() {
        return helpfulLinks;
    }
}
