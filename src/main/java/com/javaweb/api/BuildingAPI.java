package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.service.BuildingService;


@RestController 
public class BuildingAPI {
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping(value = "/api/building/")
	public List<BuildingDTO> getBuilding(@RequestParam (name = "name", required = false) String name,
			@RequestParam (name = "districtid", required = false) Long district) {
		List<BuildingDTO> result = buildingService.findAll(name, district);
		return result;
	}
	
//	@PostMapping(value = "/api/building/")
//	public Object getBuilding(@RequestBody BuildingDTO buildingDTO) {
//		//xu ly duoi DB xong roi
//		valiDate(buildingDTO);
//		return null;
//	}	
//	
//	public void valiDate(BuildingDTO buildingDTO){
//		if (buildingDTO.getName() == null || buildingDTO.getName().equals("") || buildingDTO.getNumberOfBasement() == null ) {
//			throw new FieldRequiredException("name or numberofbasement is null");
//		}
//	}
//	
//	@RequestMapping(value = "/api/building/", method = RequestMethod.PUT)
//	public void getBuilding2(@RequestParam Map<String, String> params) {
//		System.out.print(params+" ");
//	}
	
//	@RequestMapping(value = "/api/building/", method = RequestMethod.POST)
//	public void getBuidlding3(@RequestBody BuildingDTO buildingDTO) {
//		System.out.print("ok");
//	}
	
	@DeleteMapping(value =  "/api/building/{id}/{name}")
	public void deleteBuilding(@PathVariable(value = "id") String idbuilding,
				@PathVariable(value = "name") String namebuilding) {
		System.out.print("Da xoa nha co id la "+idbuilding+" "+namebuilding);
	}
}

//@RestController 
//public class BuildingAPI {
//	@GetMapping(value = "/api/building/")
//	public BuildingDTO getBuilding(@RequestParam(value = "name", required = false) String name,
//							@RequestParam(value = "numberOfBasement", required = false) Integer numberofbasement,
//							@RequestParam(value = "war", required = false) String war) {
//		BuildingDTO result = new BuildingDTO();
//		result.setName(name);
//		result.setNumberOfBasement(numberofbasement);
//		result.setWard(war);
//		return result;
//	}	
//	@PutMapping(value = "/api/building/")
//	public void getBuilding2(@RequestParam Map<String, String> params) {
//		System.out.print(params+" ");
//	}
//	
//	@PostMapping(value = "/api/building/")
//	public void getBuidlding3(@RequestBody BuildingDTO buildingDTO) {
//		System.out.print("ok");
//	}
//}