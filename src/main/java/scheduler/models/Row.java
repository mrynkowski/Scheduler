package scheduler.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Row {

    @Id
    private long id;

    @ManyToOne(targetEntity=Schedule.class)
    private Resource resource;

    @CollectionOfElements
    private List<Boolean> row;
    
	public Row() {
		super();
	}

	public Row(Resource resource, List<Boolean> row) {
		super();
		this.resource = resource;
		this.row = row;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public List<Boolean> getRow() {
		return row;
	}

	public void setRow(List<Boolean> row) {
		this.row = row;
	
	}
	
}
