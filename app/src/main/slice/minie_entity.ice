#pragma once
#include <Ice/BuiltinSequences.ice>
#include <Ice/Identity.ice>
[["cpp:include:list"]]
module minie
{
	module irpc
	{	
		exception GenericError
		{
			int errorCode;
			string errorMessage;
		};
		exception InvalidDataError extends GenericError
		{
			string field_name;
		};		
		exception VerificationError extends GenericError
		{
			
		};  

		class sys_group_rpc;
		["clr:generic:List"] sequence<sys_group_rpc> sys_group_rpc_list;

		class sys_user_rpc;
		["clr:generic:List"] sequence<sys_user_rpc> sys_user_rpc_list;

		class sys_role_rpc;
		["clr:generic:List"] sequence<sys_role_rpc> sys_role_rpc_list;

		class sys_permission_rpc;
		["clr:generic:List"] sequence<sys_permission_rpc> sys_permission_rpc_list;

		class sys_role_permission_rpc;
		["clr:generic:List"] sequence<sys_role_permission_rpc> sys_role_permission_rpc_list;

		class cm_user_rpc;
		["clr:generic:List"] sequence<cm_user_rpc> cm_user_rpc_list;

		class cm_user_defriend_rpc;
		["clr:generic:List"] sequence<cm_user_defriend_rpc> cm_user_defriend_rpc_list;
		
		class cm_user_friend_rpc;
		["clr:generic:List"] sequence<cm_user_friend_rpc> cm_user_friend_rpc_list;

		class cm_friend_request_rpc;
		["clr:generic:List"] sequence<cm_friend_request_rpc> cm_friend_request_rpc_list;

		class cm_friend_privilege_rpc;
		["clr:generic:List"] sequence<cm_friend_privilege_rpc> cm_friend_privilege_rpc_list;

		class cm_node_category_rpc;
		["clr:generic:List"] sequence<cm_node_category_rpc> cm_node_category_rpc_list;

		class cm_node_rpc;
		["clr:generic:List"] sequence<cm_node_rpc> cm_node_rpc_list;

		class cm_node_user_rpc;
		["clr:generic:List"] sequence<cm_node_user_rpc> cm_node_user_rpc_list;

		class cm_entrance_rpc;
		["clr:generic:List"] sequence<cm_entrance_rpc> cm_entrance_rpc_list;



		enum PermissionMode
		{
			pm_DENY = 0,
			pm_SELECT = 1,
			pm_UPDATE = 2,
			pm_INSERT = 4,
			pm_DELETE = 8
		};
		enum AccessMethod
		{
			NONE = 0,
			ONEPRESS = 1,
			SHOCK = 2,
			SHARP = 3,
			ACCESSCARD = 4,
			TRAFFICCARD = 5
		};
		
		["clr:property"]
		class sys_user_rpc
		{
			string id;
			string username;
			string cellphone;
			string password;
			string fullname;
			string code;
			string email;
			int level;
			sys_user_rpc creator;
			string creation_time;
		
			sys_group_rpc_list user_groups;
			sys_role_rpc_list user_roles;
		};
		
		["clr:property"]
		class sys_group_rpc
		{
			string id;
			string name;
			string desc;
			bool is_system;
			cm_node_rpc node;
			sys_user_rpc creator;
			string creation_time;

			sys_user_rpc_list group_users;
			sys_role_rpc_list group_roles;
		};
		
		["clr:property"]
		class sys_role_rpc
		{
			string name;
			string desc;
			sys_role_permission_rpc_list role_permissions;
			sys_group_rpc_list role_groups;
			sys_user_rpc_list role_users;
		};

		["clr:property"]
		class sys_permission_rpc
		{
			string name;
			string desc;
		};
		
		["clr:property"]
		class sys_role_permission_rpc
		{
			string id;
			long mode;
			["clr:attribute:XmlIgnore"]
			sys_role_rpc role;
			sys_permission_rpc perm;
			string creation_time;
			string valid_thru_time;
		};
		
		["clr:property"]
		class cm_user_rpc
		{
			string id 		;
			string username ;
			string nickname ;
			string cellphone; 
			string password ;

			string fullname ;
			string id_card 	;
			string telephone; 
			string email 	;
  
			cm_node_user_rpc_list user_nodes;
			cm_user_friend_rpc_list user_friends;
		};
				
		["clr:property"]
		class cm_user_defriend_rpc
		{
			string id;	
			cm_user_rpc self;
			cm_user_rpc blocker;
			string request_time;
		};
		["clr:property"]
		class cm_friend_request_rpc
		{
			string id;	
			cm_user_rpc from;
			cm_user_rpc to;
			string request_time;
		};

		["clr:property"]
		class cm_user_friend_rpc
		{
			string id;	
			string creation_time;
			cm_user_rpc user;
			cm_user_rpc userfriend;
			cm_friend_privilege_rpc_list friend_privileges;
		};
		
		["clr:property"]
		class cm_friend_privilege_rpc
		{
			string id;		
			cm_user_friend_rpc user_friend;
			cm_node_rpc room;
			string valid_from_time;		
			string valid_to_time;		
			string creation_time;			
		};
		["clr:property"]
		class cm_node_category_rpc
		{
			int id;
			string name;
			string desc;
		};
		["clr:property"]
		class cm_node_rpc
		{
			string id;
			string name;
			string desc;
			string address;
			cm_node_category_rpc category;
			cm_node_rpc parent;
			sys_user_rpc creator;
			string creation_time;
			
			cm_node_rpc_list children;
			sys_group_rpc_list groups;
			cm_entrance_rpc_list entrances;
			cm_node_user_rpc_list node_users;
		};
		
		["clr:property"]
		class cm_entrance_rpc
		{
			string id;
			string name;
			string desc;
			cm_node_rpc node;
			sys_user_rpc creator;
			string creation_time;
		};

		["clr:property"]
		class cm_node_user_rpc
		{
			string id;
			cm_node_rpc node;
			cm_user_rpc user;
			int host_flag;
			sys_user_rpc creator;
			string creation_time;
		};
	};
};