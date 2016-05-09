[["cpp:include:list"]]
#include "minie_service_base.ice"
module minie
{
	module irpc
	{
			
		#include <Ice/Identity.ice>
		#include <Ice/BuiltinSequences.ice>
		interface AppService;
		interface AppAuth
		{
			AppService* login_app(string un, string pw) throws AuthError;
			AppService* login_app_by_token(string token) throws AuthError;
			AppService* register_user(string cellphone, string verificationCode);
			
			bool check_cellphone(string cellphone);			// 检查手机号
			bool send_verification_email(string email);		// 发送邮件验证码
			bool send_verification_sms(string cellphone);	// 发送短信验证码

			int reset_password(string pw, string verifyCode) throws VerificationError;	//重置密码
			int reset_email(string email, string verifyCode) throws VerificationError; //重置邮箱
			int reset_cellphone(string newCellphone, string verifyCode0, string verifyCode1 ) throws VerificationError; //重置手机号?
		}; 

		interface AppService extends BaseService
		{
			/// Base Features
			cm_user_rpc get_user_info() throws GenericError;
			int update_user_info(cm_user_rpc userInfo) throws InvalidDataError;

			/// Host Features
			cm_node_user_rpc_list get_members(string nodeID) throws InvalidDataError;
			cm_node_user_rpc add_member(string nodeID, string userID) throws InvalidDataError;
			int del_member(string nodeUserID) throws InvalidDataError;
			
			/// Contracts Features
			cm_user_friend_rpc_list get_friends() throws InvalidDataError;
			int add_friends(string cellphone) throws InvalidDataError;
			int del_friends(string cellphone) throws InvalidDataError;
			cm_friend_request_rpc_list get_sent_friend_request();
			int discard_friend_request(cm_friend_request_rpc request);	//取消添加好友


			cm_friend_request_rpc_list get_recv_friend_request();
			int accept_friend_req(cm_friend_request_rpc req);
			int refuse_friend_req(cm_friend_request_rpc req);
			

			cm_user_defriend_rpc_list get_blacklist() throws GenericError;
			int add_blacklist_member(string cellphone) throws GenericError;
			int del_blacklist_member(string cellphone) throws GenericError;

			cm_friend_privilege_rpc grant_privilige(cm_user_friend_rpc friend, cm_node_rpc room, string valid_thru); // 授权 valid_thru 有效期 iso 时间 yyyy-MM-dd HH:mm:ss
			int revoke_privilige(cm_friend_privilege_rpc privilege); //取消授权
		};

	};
};
