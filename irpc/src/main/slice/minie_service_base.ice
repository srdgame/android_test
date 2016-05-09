#pragma once
#include <Ice/BuiltinSequences.ice>
#include <Ice/Identity.ice>
[["cpp:include:list"]]
#include "minie_entity.ice"
module minie
{
	module irpc
	{
	
		exception AuthError extends GenericError
		{	
		};
		enum minie_service_type
		{
			APP,
			BACKEND
		};
		class Session
		{
			string user_id;
			string token;
			string ext;
		};
		
		interface BaseService
		{
			void logout();
		};
		interface BaseServiceCallback
        {
            long heartbeat(long ticks);
        };
	};
};