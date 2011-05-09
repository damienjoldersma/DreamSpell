// DreamSpellLog.cs created with MonoDevelop
// User: damien at 10:41 AM 10/14/2008
//
// Copyright Skull Squadron, All Rights Reserved.


using EmergeTk;
using EmergeTk.Model.Security;

using log4net;
using log4net.Core;

using System;
using System.Web;

namespace DreamSpell
{
	public class DreamSpellLog : EmergeTk.EmergeTkLog
	{
		private readonly static Type declaringType = typeof(DreamSpellLog);
				
        public DreamSpellLog(ILogger logger)
            : base(logger)
		{
			// empty
		}
	}
}