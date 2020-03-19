package es.codeurjc.mastercloudapps.p3.worker;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer primaryId;

  private int id;

  private String text;

  public Integer getPrimaryId() {
    return primaryId;
  }

  public void setPrimaryId(Integer primaryId) {
    this.primaryId = primaryId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}