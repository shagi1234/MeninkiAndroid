package tm.store.meninki.data;

import java.util.ArrayList;

import tm.store.meninki.api.data.OptionDto;

public class CharactersDto {
    ArrayList<ArrayList<OptionDto>> options = new ArrayList<>();
    ArrayList<String> optionTitles = new ArrayList<>();
    ArrayList<Integer> optionTypes = new ArrayList<>();

    public ArrayList<ArrayList<OptionDto>> getOptions() {
        return options;
    }

    public ArrayList<String> getOptionTitles() {
        return optionTitles;
    }

    public void setOptionTitles(ArrayList<String> optionTitles) {
        this.optionTitles = optionTitles;
    }

    public void setOptions(ArrayList<ArrayList<OptionDto>> options) {
        this.options = options;
    }

    public ArrayList<Integer> getOptionTypes() {
        return optionTypes;
    }

    public void setOptionTypes(ArrayList<Integer> optionTypes) {
        this.optionTypes = optionTypes;
    }
}
