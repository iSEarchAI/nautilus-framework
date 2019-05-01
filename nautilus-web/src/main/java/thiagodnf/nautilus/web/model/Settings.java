package thiagodnf.nautilus.web.model;

import com.google.gson.Gson;

import thiagodnf.nautilus.core.colorize.DontColorize;
import thiagodnf.nautilus.core.correlation.DontCorrelation;
import thiagodnf.nautilus.core.duplicated.ByObjectivesDuplicatesRemover;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.reduction.ConfiabilityBasedReduction;

public class Settings {

	private boolean showLines = true;
	
	private String colorizeId = new DontColorize().getId();
	
	private String normalizeId = new ByParetoFrontValuesNormalize().getId();
	
	private String correlationId = new DontCorrelation().getId();
	
	private String duplicatesRemoverId = new ByObjectivesDuplicatesRemover().getId();
	
	private String reducerId = new ConfiabilityBasedReduction().getId();
	
	private String name;

	public boolean getShowLines() {
		return showLines;
	}

	public void setShowLines(boolean showLines) {
		this.showLines = showLines;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getColorizeId() {
		return colorizeId;
	}

	public void setColorizeId(String colorizeId) {
		this.colorizeId = colorizeId;
	}

	public String getNormalizeId() {
		return normalizeId;
	}

	public void setNormalizeId(String normalizeId) {
		this.normalizeId = normalizeId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getDuplicatesRemoverId() {
		return duplicatesRemoverId;
	}

	public void setDuplicatesRemoverId(String duplicatesRemoverId) {
		this.duplicatesRemoverId = duplicatesRemoverId;
	}
	
	public String getReducerId() {
		return reducerId;
	}

	public void setReducerId(String reducerId) {
		this.reducerId = reducerId;
	}

	public String toString() {
		return new Gson().toJson(this);
	}
}
