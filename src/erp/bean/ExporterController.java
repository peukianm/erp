/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.bean;



import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * ExporterController
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @version $Revision$
 * @since 1.0
 */
@Named
@ApplicationScoped
public class ExporterController implements Serializable {

	private static final long serialVersionUID = 20120316L;

	private Boolean customExporter;

	public ExporterController() {
		customExporter = false;
	}

	public Boolean getCustomExporter() {
		return customExporter;
	}

	public void setCustomExporter(final Boolean customExporter) {
		this.customExporter = customExporter;
	}
}