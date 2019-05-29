package uet.vnu.checkin;

public class Participant
{
    private String id;
    private String name;
    private String dateOfBirth;
    private String type;
    private boolean checkedIn;

    public Participant(String _id, String _name, String _dateOfBirth, String _type, boolean _checkedIn)
    {
        this.id = _id;
        this.name = _name;
        this.dateOfBirth = _dateOfBirth;
        this.type = _type;
        this.checkedIn = _checkedIn;
    }

    /* Firebase requires following methods for setting/getting values */
    public Participant()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
