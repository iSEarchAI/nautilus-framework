package thiagodnf.nautilus.plugin.extension;

import org.pf4j.ExtensionPoint;

public interface FormatterExtension extends ExtensionPoint {

	public String formatInstanceFile(String problemId, String rawContent);
}
