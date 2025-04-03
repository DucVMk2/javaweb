package com.javaweb.api;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.service.BuildingService;


@RestController 
@PropertySource("classpath:application.properties")
@Transactional
public class BuildingAPI {
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Value("${dev.nguyen}")
	private String data;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	//Chức năng Tìm kiếm
	@GetMapping(value = "/api/building/")
	public List<BuildingDTO> getBuilding(@RequestParam Map<String, Object> params,
										@RequestParam(name="typeCode", required = false) List<String> typeCode) {
		List<BuildingDTO> result = buildingService.findAll(params, typeCode);
		return result;
	}
	
	//Demo JPA
//	@PostMapping(value = "/api/building/")
//	public void createBuilding(@RequestBody BuildingRequestDTO buildingRequestDTO) {
//		BuildingEntity buildEntity = new BuildingEntity();
//		buildEntity.setName(buildingRequestDTO.getName());
//		buildEntity.setNumberOfBasement(buildingRequestDTO.getNumberOfBasement());
//		buildEntity.setWard(buildingRequestDTO.getWard());
//		buildEntity.setStreet(buildingRequestDTO.getStreet());
//		buildEntity.setFloorArea(buildingRequestDTO.getFloorArea());
//		buildEntity.setRentPrice(buildingRequestDTO.getRentPrice());
//		buildEntity.setManagerName(buildingRequestDTO.getManagerName());
//		buildEntity.setManagerPhonenumber(buildingRequestDTO.getManagerPhonenumber());
//		buildEntity.setServiceFee(buildingRequestDTO.getServiceFee());
//		buildEntity.setBrokerageFee(buildingRequestDTO.getBrokerageFee());
//		DistrictEntity districtEntity = new DistrictEntity();
//		districtEntity.setId(buildingRequestDTO.getDistrictId());
//		buildEntity.setDistrict(districtEntity);
//		entityManager.persist(buildEntity);
//		System.out.print("ok");
//	}	
	
	//Demo JPA
//	@PutMapping(value = "/api/building/")
//	public void updateBuilding(@RequestBody BuildingRequestDTO buildingRequestDTO) {
//		BuildingEntity buildEntity = new BuildingEntity();
//		buildEntity.setId(3L);
//		buildEntity.setName(buildingRequestDTO.getName());
//		buildEntity.setNumberOfBasement(buildingRequestDTO.getNumberOfBasement());
//		buildEntity.setWard(buildingRequestDTO.getWard());
//		buildEntity.setStreet(buildingRequestDTO.getStreet());
//		buildEntity.setFloorArea(buildingRequestDTO.getFloorArea());
//		buildEntity.setRentPrice(buildingRequestDTO.getRentPrice());
//		buildEntity.setManagerName(buildingRequestDTO.getManagerName());
//		buildEntity.setManagerPhonenumber(buildingRequestDTO.getManagerPhonenumber());
//		buildEntity.setServiceFee(buildingRequestDTO.getServiceFee());
//		buildEntity.setBrokerageFee(buildingRequestDTO.getBrokerageFee());
//		DistrictEntity districtEntity = new DistrictEntity();
//		districtEntity.setId(buildingRequestDTO.getDistrictId());
//		buildEntity.setDistrict(districtEntity);
//		entityManager.merge(buildEntity);
//		System.out.print("ok");	
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
	
	//Demo JPA
//	@DeleteMapping(value =  "/api/building/{id}")
//	public void deleteBuilding(@PathVariable Long id) {
//		BuildingEntity buildingEntity = entityManager.find(BuildingEntity.class, id);
//		entityManager.remove(buildingEntity);
//		System.out.print(data);
//	}
	
	//Demo Spring DataJPA
	@GetMapping(value = "/api/building/{name}/{street}")
	public BuildingDTO getBuildingById(@PathVariable String name,
										@PathVariable String street) {
		BuildingDTO result = new BuildingDTO();
		List<BuildingEntity> building = buildingRepository.findByNameContainingAndStreet(name, street);
		return result;
	}
	
	@DeleteMapping(value = "/api/building/{id}")
	public void deleteBuilding(@PathVariable Long[] id) {
		buildingRepository.deleteByIdIn(id);
	}
	
	@PutMapping(value = "/api/building/")
	public void updateBuilding(@RequestBody BuildingRequestDTO buildingRequestDTO) {
		BuildingEntity buildEntity = new BuildingEntity();
		buildEntity.setName(buildingRequestDTO.getName());
		buildEntity.setNumberOfBasement(buildingRequestDTO.getNumberOfBasement());
		buildEntity.setWard(buildingRequestDTO.getWard());
		buildEntity.setStreet(buildingRequestDTO.getStreet());
		buildEntity.setFloorArea(buildingRequestDTO.getFloorArea());
		buildEntity.setRentPrice(buildingRequestDTO.getRentPrice());
		buildEntity.setManagerName(buildingRequestDTO.getManagerName());
		buildEntity.setManagerPhonenumber(buildingRequestDTO.getManagerPhonenumber());
		buildEntity.setServiceFee(buildingRequestDTO.getServiceFee());
		buildEntity.setBrokerageFee(buildingRequestDTO.getBrokerageFee());
		DistrictEntity districtEntity = new DistrictEntity();
		districtEntity.setId(buildingRequestDTO.getDistrictId());
		buildEntity.setDistrict(districtEntity);
		buildingRepository.save(buildEntity);
		System.out.print("ok");	
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