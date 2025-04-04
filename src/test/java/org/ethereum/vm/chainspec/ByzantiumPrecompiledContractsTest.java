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
package org.ethereum.vm.chainspec;

import static org.ethereum.vm.util.ByteArrayUtil.EMPTY_BYTE_ARRAY;
import static org.ethereum.vm.util.ByteArrayUtil.bytesToBigInteger;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.ethereum.vm.DataWord;
import org.ethereum.vm.client.Repository;
import org.ethereum.vm.crypto.ECKey;
import org.ethereum.vm.program.InternalTransaction;
import org.ethereum.vm.program.ProgramResult;
import org.ethereum.vm.util.HexUtil;
import org.junit.Test;

public class ByzantiumPrecompiledContractsTest {

    ByzantiumPrecompiledContracts precompiledContracts = new ByzantiumPrecompiledContracts();

    private PrecompiledContractContext wrapData(byte[] data) {
        return new PrecompiledContractContext() {

            private Repository repo = mock(Repository.class);
            private ProgramResult result = mock(ProgramResult.class);
            private InternalTransaction tx = mock(InternalTransaction.class);
            {
                when(tx.getData()).thenReturn(data);
            }

            @Override
            public Repository getTrack() {
                return repo;
            }

            @Override
            public ProgramResult getResult() {
                return result;
            }

            @Override
            public InternalTransaction getInternalTransaction() {
                return tx;
            }
        };
    }

    @Test
    public void identityTest1() {
        DataWord addr = DataWord.of("0000000000000000000000000000000000000000000000000000000000000004");
        PrecompiledContract contract = precompiledContracts.getContractForAddress(addr);
        byte[] data = HexUtil.fromHexString("112233445566");
        byte[] expected = HexUtil.fromHexString("112233445566");

        byte[] result = contract.execute(wrapData(data)).getRight();

        assertArrayEquals(expected, result);
    }

    @Test
    public void sha256Test1() {
        DataWord addr = DataWord.of("0000000000000000000000000000000000000000000000000000000000000002");
        PrecompiledContract contract = precompiledContracts.getContractForAddress(addr);
        byte[] data = null;
        String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        byte[] result = contract.execute(wrapData(data)).getRight();

        assertEquals(expected, HexUtil.toHexString(result));
    }

    @Test
    public void sha256Test2() {

        DataWord addr = DataWord.of("0000000000000000000000000000000000000000000000000000000000000002");
        PrecompiledContract contract = precompiledContracts.getContractForAddress(addr);
        byte[] data = EMPTY_BYTE_ARRAY;
        String expected = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        byte[] result = contract.execute(wrapData(data)).getRight();

        assertEquals(expected, HexUtil.toHexString(result));
    }

    @Test
    public void sha256Test3() {

        DataWord addr = DataWord.of("0000000000000000000000000000000000000000000000000000000000000002");
        PrecompiledContract contract = precompiledContracts.getContractForAddress(addr);
        byte[] data = HexUtil.fromHexString("112233");
        String expected = "49ee2bf93aac3b1fb4117e59095e07abe555c3383b38d608da37680a406096e8";

        byte[] result = contract.execute(wrapData(data)).getRight();

        assertEquals(expected, HexUtil.toHexString(result));
    }

    @Test
    public void Ripempd160Test1() {
        DataWord addr = DataWord.of("0000000000000000000000000000000000000000000000000000000000000003");
        PrecompiledContract contract = precompiledContracts.getContractForAddress(addr);
        byte[] data = HexUtil.fromHexString("0000000000000000000000000000000000000000000000000000000000000001");
        String expected = "000000000000000000000000ae387fcfeb723c3f5964509af111cf5a67f30661";

        byte[] result = contract.execute(wrapData(data)).getRight();

        assertEquals(expected, HexUtil.toHexString(result));
    }

    @Test
    public void ecRecoverTest1() throws ECKey.SignatureException {
        byte[] messageHash = HexUtil.fromHexString("14431339128bd25f2c7f93baa611e367472048757f4ad67f6d71a5ca0da550f5");
        byte v = 28;
        byte[] r = HexUtil.fromHexString("51e4dbbbcebade695a3f0fdf10beb8b5f83fda161e1a3105a14c41168bf3dce0");
        byte[] s = HexUtil.fromHexString("46eabf35680328e26ef4579caf8aeb2cf9ece05dbf67a4f3d1f28c7b1d0e3546");
        byte[] address = ECKey.signatureToAddress(messageHash, ECKey.ECDSASignature.fromComponents(r, s, v));

        String expected = "7f8b3b04bf34618f4a1723fba96b5db211279a2b";
        assertEquals(expected, HexUtil.toHexString(address));

        byte[] data = HexUtil.fromHexString("14431339128bd25f2c7f93baa611e367"
                + "472048757f4ad67f6d71a5ca0da550f5"
                + "00000000000000000000000000000000"
                + "0000000000000000000000000000001c"
                + "51e4dbbbcebade695a3f0fdf10beb8b5"
                + "f83fda161e1a3105a14c41168bf3dce0"
                + "46eabf35680328e26ef4579caf8aeb2c"
                + "f9ece05dbf67a4f3d1f28c7b1d0e3546");
        DataWord addr = DataWord.of("0000000000000000000000000000000000000000000000000000000000000001");
        PrecompiledContract contract = precompiledContracts.getContractForAddress(addr);
        String expected2 = "0000000000000000000000007f8b3b04bf34618f4a1723fba96b5db211279a2b";

        byte[] result = contract.execute(wrapData(data)).getRight();
        assertEquals(expected2, HexUtil.toHexString(result));
    }

    @Test
    public void modExpTest() {
        DataWord addr = DataWord.of("0000000000000000000000000000000000000000000000000000000000000005");
        PrecompiledContract contract = precompiledContracts.getContractForAddress(addr);
        assertNotNull(contract);

        byte[] data1 = HexUtil.fromHexString(
                "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "03" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f");

        assertEquals(13056, contract.getGasForData(data1));

        byte[] res1 = contract.execute(wrapData(data1)).getRight();
        assertEquals(32, res1.length);
        assertEquals(BigInteger.ONE, bytesToBigInteger(res1));

        byte[] data2 = HexUtil.fromHexString(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2e" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f");

        assertEquals(13056, contract.getGasForData(data2));

        byte[] res2 = contract.execute(wrapData(data2)).getRight();
        assertEquals(32, res2.length);
        assertEquals(BigInteger.ZERO, bytesToBigInteger(res2));

        byte[] data3 = HexUtil.fromHexString(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd");

        // hardly imagine this value could be a real one
        assertEquals(3_674_950_435_109_146_392L, contract.getGasForData(data3));

        byte[] data4 = HexUtil.fromHexString(
                "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "03" +
                        "ffff" +
                        "8000000000000000000000000000000000000000000000000000000000000000" +
                        "07"); // "07" should be ignored by data parser

        assertEquals(768, contract.getGasForData(data4));

        byte[] res4 = contract.execute(wrapData(data4)).getRight();
        assertEquals(32, res4.length);
        assertEquals(new BigInteger("26689440342447178617115869845918039756797228267049433585260346420242739014315"),
                bytesToBigInteger(res4));

        byte[] data5 = HexUtil.fromHexString(
                "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "03" +
                        "ffff" +
                        "80"); // "80" should be parsed as
                               // "8000000000000000000000000000000000000000000000000000000000000000"
        // cause call data is infinitely right-padded with zero bytes

        assertEquals(768, contract.getGasForData(data5));

        byte[] res5 = contract.execute(wrapData(data5)).getRight();
        assertEquals(32, res5.length);
        assertEquals(new BigInteger("26689440342447178617115869845918039756797228267049433585260346420242739014315"),
                bytesToBigInteger(res5));

        // check overflow handling in gas calculation
        byte[] data6 = HexUtil.fromHexString(
                "0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000020000000000000000000000000000000" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd");

        assertEquals(Long.MAX_VALUE, contract.getGasForData(data6));

        // check rubbish data
        byte[] data7 = HexUtil.fromHexString(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd");

        assertEquals(Long.MAX_VALUE, contract.getGasForData(data7));

        // check empty data
        byte[] data8 = new byte[0];

        assertEquals(0, contract.getGasForData(data8));

        byte[] res8 = contract.execute(wrapData(data8)).getRight();
        assertArrayEquals(EMPTY_BYTE_ARRAY, res8);

        assertEquals(0, contract.getGasForData(null));
        assertArrayEquals(EMPTY_BYTE_ARRAY, contract.execute(wrapData(null)).getRight());
    }
}
