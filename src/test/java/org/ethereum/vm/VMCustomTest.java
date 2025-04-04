/**
 * Copyright (c) [2018] [ The Semux Developers ]
 * Copyright (c) [2016] [ <ether.camp> ]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ethereum.vm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.ethereum.vm.program.Program;
import org.ethereum.vm.program.exception.IllegalOperationException;
import org.ethereum.vm.program.exception.OutOfGasException;
import org.ethereum.vm.program.exception.StackUnderflowException;
import org.ethereum.vm.util.HexUtil;
import org.junit.Before;
import org.junit.Test;

public class VMCustomTest extends TestBase {

    private byte[] callData = HexUtil.fromHexString("00000000000000000000000000000000000000000000000000000000000000a1" +
            "00000000000000000000000000000000000000000000000000000000000000b1");

    @Before
    public void additionalSetup() {
        invoke = spy(invoke);
        when(invoke.getData()).thenReturn(callData);
        repository.addBalance(address, BigInteger.valueOf(1000L));
    }

    @Test // CALLDATASIZE OP
    public void testCALLDATASIZE_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("36"), invoke);
        String s_expected_1 = DataWord.of(callData.length).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // CALLDATALOAD OP
    public void testCALLDATALOAD_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("600035"), invoke);
        String s_expected_1 = "00000000000000000000000000000000000000000000000000000000000000a1";

        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // CALLDATALOAD OP
    public void testCALLDATALOAD_2() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("600235"), invoke);
        String s_expected_1 = "0000000000000000000000000000000000000000000000000000000000a10000";

        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // CALLDATALOAD OP
    public void testCALLDATALOAD_3() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("602035"), invoke);
        String s_expected_1 = "00000000000000000000000000000000000000000000000000000000000000b1";

        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // CALLDATALOAD OP
    public void testCALLDATALOAD_4() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("602335"), invoke);
        String s_expected_1 = "00000000000000000000000000000000000000000000000000000000b1000000";

        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // CALLDATALOAD OP
    public void testCALLDATALOAD_5() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("603F35"), invoke);
        String s_expected_1 = "b100000000000000000000000000000000000000000000000000000000000000";

        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test(expected = RuntimeException.class) // CALLDATALOAD OP mal
    public void testCALLDATALOAD_6() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("35"), invoke);
        try {
            vm.step(program);
        } finally {
            assertTrue(program.isStopped());
        }
    }

    @Test // CALLDATACOPY OP
    public void testCALLDATACOPY_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("60206000600037"), invoke);
        String m_expected = "00000000000000000000000000000000000000000000000000000000000000a1";

        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);

        assertEquals(m_expected, HexUtil.toHexString(program.getMemory()));
    }

    @Test // CALLDATACOPY OP
    public void testCALLDATACOPY_2() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("60406000600037"), invoke);
        String m_expected = "00000000000000000000000000000000000000000000000000000000000000a1" +
                "00000000000000000000000000000000000000000000000000000000000000b1";

        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);

        assertEquals(m_expected, HexUtil.toHexString(program.getMemory()));
    }

    @Test // CALLDATACOPY OP
    public void testCALLDATACOPY_3() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("60406004600037"), invoke);
        String m_expected = "000000000000000000000000000000000000000000000000000000a100000000" +
                "000000000000000000000000000000000000000000000000000000b100000000";

        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);

        assertEquals(m_expected, HexUtil.toHexString(program.getMemory()));
    }

    @Test // CALLDATACOPY OP
    public void testCALLDATACOPY_4() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("60406000600437"), invoke);
        String m_expected = "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000a100000000000000000000000000000000000000000000000000000000" +
                "000000b100000000000000000000000000000000000000000000000000000000";

        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);

        assertEquals(m_expected, HexUtil.toHexString(program.getMemory()));
    }

    @Test // CALLDATACOPY OP
    public void testCALLDATACOPY_5() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("60406000600437"), invoke);
        String m_expected = "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000a100000000000000000000000000000000000000000000000000000000" +
                "000000b100000000000000000000000000000000000000000000000000000000";

        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);

        assertEquals(m_expected, HexUtil.toHexString(program.getMemory()));
    }

    @Test(expected = StackUnderflowException.class) // CALLDATACOPY OP mal
    public void testCALLDATACOPY_6() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("6040600037"), invoke);

        try {
            vm.step(program);
            vm.step(program);
            vm.step(program);
        } finally {
            assertTrue(program.isStopped());
        }
    }

    @Test(expected = OutOfGasException.class) // CALLDATACOPY OP mal
    public void testCALLDATACOPY_7() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("6020600073CC0929EB16730E7C14FEFC63006AC2D794C5795637"), invoke);

        try {
            vm.step(program);
            vm.step(program);
            vm.step(program);
            vm.step(program);
        } finally {
            assertTrue(program.isStopped());
        }
    }

    @Test // ADDRESS OP
    public void testADDRESS_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("30"), invoke);
        String s_expected_1 = DataWord.of(address).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // BALANCE OP
    public void testBALANCE_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("3031"), invoke);
        String s_expected_1 = DataWord.of(1000L).toString();

        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // ORIGIN OP
    public void testORIGIN_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("32"), invoke);
        String s_expected_1 = DataWord.of(origin).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // CALLER OP
    public void testCALLER_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("33"), invoke);
        String s_expected_1 = DataWord.of(caller).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // CALLVALUE OP
    public void testCALLVALUE_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("34"), invoke);
        String s_expected_1 = DataWord.of(value).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // SHA3 OP
    public void testSHA3_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("60016000536001600020"), invoke);
        String s_expected_1 = "5fe7f977e71dba2ea1a68e21057beebb9be2ac30c6410aa38d4f3fbe41dcffd2";

        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // SHA3 OP
    public void testSHA3_2() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("6102016000526002601E20"), invoke);
        String s_expected_1 = "114a3fe82a0219fcc31abd15617966a125f12b0fd3409105fc83b487a9d82de4";

        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test(expected = StackUnderflowException.class) // SHA3 OP mal
    public void testSHA3_3() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("610201600052600220"), invoke);
        try {
            vm.step(program);
            vm.step(program);
            vm.step(program);
            vm.step(program);
            vm.step(program);
        } finally {
            assertTrue(program.isStopped());
        }
    }

    @Test // BLOCKHASH OP
    public void testBLOCKHASH_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("600140"), invoke);
        String s_expected_1 = DataWord.of(blockStore.getBlockHashByNumber(1)).toString();

        vm.step(program);
        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // COINBASE OP
    public void testCOINBASE_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("41"), invoke);
        String s_expected_1 = DataWord.of(coinbase).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // TIMESTAMP OP
    public void testTIMESTAMP_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("42"), invoke);
        String s_expected_1 = DataWord.of(timestamp).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // NUMBER OP
    public void testNUMBER_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("43"), invoke);
        String s_expected_1 = DataWord.of(number).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // DIFFICULTY OP
    public void testDIFFICULTY_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("44"), invoke);
        String s_expected_1 = DataWord.of(difficulty).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // GASPRICE OP
    public void testGASPRICE_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("3A"), invoke);
        String s_expected_1 = DataWord.of(gasPrice).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // GAS OP
    public void testGAS_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("5A"), invoke);
        String s_expected_1 = DataWord.of(gas - OpCode.GAS.getTier().asInt()).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test // GASLIMIT OP
    public void testGASLIMIT_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("45"), invoke);
        String s_expected_1 = DataWord.of(gasLimit).toString();

        vm.step(program);

        DataWord item1 = program.stackPop();
        assertEquals(s_expected_1, item1.toString());
    }

    @Test(expected = IllegalOperationException.class) // INVALID OP
    public void testINVALID_1() {
        VM vm = new VM();
        program = new Program(HexUtil.fromHexString("60012F6002"), invoke);
        String s_expected_1 = "0000000000000000000000000000000000000000000000000000000000000001";

        try {
            vm.step(program);
            vm.step(program);
        } finally {
            assertTrue(program.isStopped());
            DataWord item1 = program.stackPop();
            assertEquals(s_expected_1, item1.toString());
        }
    }
}