//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author gvandenbekerom
 * @since 13-Dec-18
 */
@Configuration
@ComponentScan("com.inthergroup.webpoc")
@EntityScan("com.inthergroup.webpoc")
@EnableJpaRepositories("com.inthergroup.webpoc.framework.repositories")
public class CoreSharedContext {}
