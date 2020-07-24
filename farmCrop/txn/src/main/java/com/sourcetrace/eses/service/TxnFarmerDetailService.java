package com.sourcetrace.eses.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcetrace.eses.util.FileUtil;
import com.sourcetrace.eses.util.ObjectUtil;
import com.sourcetrace.esesw.entity.profile.Farmer;
import com.sourcetrace.esesw.entity.profile.FarmerJsonElement;

@Component
@Path("/")
@Produces({ "application/json", "application/xml", MediaType.TEXT_XML })
@WebService(serviceName = "TxnFarmerDetailService", name = "TxnFarmerDetailService", targetNamespace = "http://www.sourcetrace.com")
public class TxnFarmerDetailService {
	@Autowired
	private IFarmerService farmerService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("/Farmers")
	@Produces({ MediaType.APPLICATION_JSON ,})
	public List<FarmerJsonElement> FarmersData() {
		List<FarmerJsonElement> jsList = new ArrayList<>();

				List<java.lang.Object[]> farmerList = farmerService.listFarmerInfo();
			if (!ObjectUtil.isListEmpty(farmerList)) {

				for (java.lang.Object[] farmer : farmerList) {
					FarmerJsonElement fs = new FarmerJsonElement();
					fs.setName(farmer[3].toString() + farmer[4] + farmer[5]);
					fs.setId(Long.valueOf(farmer[0].toString()));
					fs.setFarmerCode(farmer[2] != null ? farmer[2].toString() : "");
					fs.setVillage(farmer[6] != null ? farmer[6].toString() : "");

					jsList.add(fs);
				}
			}
			
		return jsList;

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("/FarmerImage/{id}")
	@Produces({ MediaType.APPLICATION_JSON,"image/jpg" })
	public Response FarmersDataImageById(@PathParam("id") Long id) throws IOException {
		Farmer f = farmerService.findFarmerById(id);
		File file =  new File(FileUtil.storeXls(String.valueOf(id)), "verificationImage" + id + ".jpg");
		FileUtils.writeByteArrayToFile(file, f.getImageInfo().getPhoto().getImage());
	
	    return Response.ok(file, "image/jpg").header("Inline", "filename=\"" + file.getName() + "\"")
	            .build();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("/Farmers/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public FarmerJsonElement FarmersDataById(@PathParam("id") Long id) {
		FarmerJsonElement fs = new FarmerJsonElement();
		if (id != null && id > 0) {
			Farmer f = farmerService.findFarmerById(id);
			
			fs.setName(f.getName());
			fs.setId(f.getId());
			fs.setFarmerCode(f.getFarmerCode());
			fs.setVillage(f.getVillage().getName());

			
		} 
		return fs;

	}
}
