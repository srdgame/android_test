#pragma once
#include <Ice/BuiltinSequences.ice>
#include <Ice/Identity.ice>
[["cpp:include:list"]]
#include "minie_service_base.ice"
module minie
{
	module irpc
	{
        interface EntranceService;
        
        interface EntranceServiceCallback;
        
		interface EntranceServiceAuth
		{
			EntranceService* login(string sn, string pw) throws AuthError;
			EntranceService* login_by_token(string token) throws AuthError;
		};
		interface EntranceService extends BaseService
		{
            cm_entrance_rpc get_context();
            void subscribe(Ice::Identity ident, string param);
            
            Ice::StringSeq get_verification_codes();
            int request(string verifyCode);
        };
        interface EntranceServiceCallback extends BaseServiceCallback
        {
            int add_verifycode(string code);
            int del_verifycode(string code);
            int open_door();
            int notify_message(string msg);
        };
    };
};