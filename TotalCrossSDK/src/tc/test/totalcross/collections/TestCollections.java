/*********************************************************************************
 *  TotalCross Software Development Kit - Litebase                               *
 *  Copyright (C) 2000-2012 SuperWaba Ltda.                                      *
 *  All Rights Reserved                                                          *
 *                                                                               *
 *  This library and virtual machine is distributed in the hope that it will     *
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                         *
 *                                                                               *
 *********************************************************************************/

package tc.test.totalcross.collections;

import totalcross.sys.*;
import totalcross.unit.TestSuite;

public class TestCollections extends TestSuite
{

   public TestCollections()
   {
      super("Litebase Test Suite");
      if (Settings.platform.equals(Settings.ANDROID))
         Vm.debug(Vm.ALTERNATIVE_DEBUG);
      
      // Tests exceptions.
      addTestCase(TestConcurrentModificationException.class);
      addTestCase(TestNoSuchElementException.class);
      
      // Tests interfaces.
      addTestCase(TestCollection.class);
      addTestCase(TestComparator.class);
      addTestCase(TestEnumeration.class);
      addTestCase(TestIterator.class);
      addTestCase(TestList.class);
      addTestCase(TestListIterator.class);
      addTestCase(TestMap.class);
      addTestCase(TestRandomAccess.class);
      addTestCase(TestSet.class);
      addTestCase(TestSortedMap.class);
      addTestCase(TestSortedSet.class);
      
      // Abstract classes.
      addTestCase(TestAbstractCollection.class);
      addTestCase(TestAbstractList.class);
      addTestCase(TestAbstractMap.class);
      addTestCase(TestAbstractSequentialList.class);
      addTestCase(TestAbstractSet.class);
      
      addTestCase(TestGenerics.class);
   }
}
