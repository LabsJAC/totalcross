/*********************************************************************************
 *  TotalCross Software Development Kit                                          *
 *  Copyright (C) 2000-2012 SuperWaba Ltda.                                      *
 *  All Rights Reserved                                                          *
 *                                                                               *
 *  This library and virtual machine is distributed in the hope that it will     *
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                         *
 *                                                                               *
 *********************************************************************************/



#include "tcvm.h"
#include "../../axtls/crypto.h"

//////////////////////////////////////////////////////////////////////////
TC_API void tcdSHA256D_nativeCreate(NMParams p) // totalcross/crypto/digest/SHA256Digest native void nativeCreate();
{
   Object sha256dObj = p->obj[0];
   Object digestObj;

   if ((digestObj = createByteArray(p->currentContext, sizeof(SHA256_CTX))) != null)
   {
      *Digest_digestRef(sha256dObj) = digestObj;
      setObjectLock(digestObj, UNLOCKED);
   }
}
//////////////////////////////////////////////////////////////////////////
TC_API void tcdSHA256D_process_B(NMParams p) // totalcross/crypto/digest/SHA256Digest native protected final byte[] process(byte []data);
{
   Object sha256dObj = p->obj[0];
   Object dataObj = p->obj[1];
   Object digestObj = *Digest_digestRef(sha256dObj);
   SHA256_CTX *ctx = (SHA256_CTX*) ARRAYOBJ_START(digestObj);

   Object byteArrayResult;

   if (!dataObj)
   {
      throwNullArgumentException(p->currentContext, "data");
      return;
   }

   SHA256Init(ctx);
   SHA256Update(ctx, ARRAYOBJ_START(dataObj), ARRAYOBJ_LEN(dataObj));

   if ((byteArrayResult = createByteArray(p->currentContext, SHA256_SIZE)) != null)
   {
      p->retO = byteArrayResult;
      setObjectLock(byteArrayResult, UNLOCKED);
      SHA256Final(ctx, ARRAYOBJ_START(byteArrayResult));
   }
}