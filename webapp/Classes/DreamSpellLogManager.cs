// DreamSpellLogManager.cs created with MonoDevelop
// User: damien at 10:41 AM 10/14/2008
//
// Copyright Skull Squadron, All Rights Reserved.

using log4net;
using System;
using System.Collections.Generic;

namespace DreamSpell
{       
	public class DreamSpellLogManager
	{
		
		public DreamSpellLogManager()
		{
		}
		
		public static DreamSpellLog GetLogger(Type type)
		{
			return new DreamSpellLog(LogManager.GetLogger(type).Logger);
		}
		
	}
}

