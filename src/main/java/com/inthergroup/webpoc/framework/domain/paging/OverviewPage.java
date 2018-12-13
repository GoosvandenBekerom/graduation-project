//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.domain.paging;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author gvandenbekerom
 * @since 16-Oct-18
 *
 * Used as a wrapper for a single page of overview data
 */
public @Data class OverviewPage {
    private List<Map<String, Object>> content;
    private int page;
    private int size;
    private int totalCount;

    public OverviewPage(List<Map<String, Object>> content, int page, int size, int totalCount) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
    }
}
