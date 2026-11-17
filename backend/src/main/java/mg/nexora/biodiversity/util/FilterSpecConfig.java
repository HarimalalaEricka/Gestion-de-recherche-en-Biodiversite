package mg.nexora.biodiversity.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilterSpecConfig {

    public FilterSpecConfig(@Value("${app.filter.max-depth:6}") int maxDepth) {
        FilterSpec.setMaxDepth(maxDepth);
    }
}