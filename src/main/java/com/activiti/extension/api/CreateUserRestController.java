
package com.activiti.extension.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.activiti.domain.idm.AccountType;
import com.activiti.domain.idm.Group;
import com.activiti.domain.idm.User;
import com.activiti.domain.idm.UserStatus;
import com.activiti.model.UserDetails;
import com.activiti.service.api.GroupService;
import com.activiti.service.api.UserService;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/enterprise")
public class CreateUserRestController {


    @Autowired
    UserService userService;
    
    @Autowired
    GroupService groupService;
    
    @Autowired
    IdentityService identityService;
    
    @Timed
    @RequestMapping(value = "/createUser", method= RequestMethod.GET)
	public String createUser(
			@RequestParam(value = "path", required = false, defaultValue = "/src/main/java/UserList.csv") String path) {
        System.out.println("File Path: "+path);
        Long tenantId = 1L;
        StringBuffer stringBuffer = new StringBuffer("Executed");
		
			List<UserDetails> dataFromCSVFile = getDataFromCSVFile(path);
			for (UserDetails userDetails : dataFromCSVFile) {
				try {
				User findUserByEmail = userService.findUserByEmail(userDetails.getEmail());
				if(findUserByEmail == null){
					User user = userService.createNewUser(userDetails.getEmail(),
							userDetails.getFirstName(), userDetails.getLastName(),
							userDetails.getPassword() == null? userDetails.getFirstName():userDetails.getPassword() , userDetails.getCompany(),
									userDetails.getInitialStatus(), AccountType.ENTERPRISE, tenantId);
					stringBuffer.append("<BR> New User Created : "+user.getEmail());
					
					
						String userGroup = userDetails.getUserGroup();
					System.out.println("User Group: "+userGroup);
					
					
					
					if(userGroup != null){
						String[] groups = userGroup.split(";");
						for(String group :groups){
							List<Group> functionalGroups = groupService.getFunctionalGroups(tenantId);
							
							System.out.println("Total number of groups : "+functionalGroups.size());
							
							for(Group group1 :functionalGroups){
								System.out.println(group1.getName()+ " : "+group1.getId());
								
								if(group1.getName().equalsIgnoreCase(group)){
									System.out.println("Adding to group.. "+group1.getName());
									groupService.addUserToGroup(group1, user);
								       
								}
								
							}
							
						}
					}
					
				}else{
					stringBuffer.append("<BR> Already Exist: "+findUserByEmail.getEmail()+ " Status: "+findUserByEmail.getStatus());
				}
				} catch (Exception e) {
					e.printStackTrace();
					stringBuffer.append("<BR> Error : "+e.getMessage());
				}
			}
		

		return stringBuffer.toString();
	}
    
	private static List<UserDetails> getDataFromCSVFile(String filePath) {

		String line = "";
		String cvsSplitBy = ",";
		List<UserDetails> userDetailList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			boolean isHeader = true;
			while ((line = br.readLine()) != null) {
				if (!isHeader) {
					UserDetails userDetails = new UserDetails();
					// use comma as separator
					String[] userList = line.split(cvsSplitBy);
					for (String userDetail : userList) {
						System.out.println(userDetail);
					}
					try {
						userDetails.setEmail(userList[0]);
						userDetails.setFirstName(userList[1]);
						userDetails.setLastName(userList[2]);
						userDetails.setCompany(userList[3]);
						userDetails.setUserGroup(userList[4]);
						//userDetails.setPassword(userList[4]);
						if(userList.length > 5 ){
						String initialStatus = userList[5];
						System.out.println("initialStatus : "+initialStatus);
						if(initialStatus != null && initialStatus.length()>1 ){
							
							userDetails.setInitialStatus(UserStatus.valueOf(initialStatus.toUpperCase()));	
						}else{
							userDetails.setInitialStatus(UserStatus.ACTIVE);
						}
					}
						
						/*userDetails.setAccountType(userList[6]);
						userDetails.setTenantId(userList[7]);*/
					} catch (Exception e) {
						e.printStackTrace();
					}
					userDetailList.add(userDetails);
				} else {
					isHeader = false;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return userDetailList;
	}
 }
