/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2018 nuls.io
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.token.base;

import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.annotation.View;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static io.nuls.contract.sdk.Utils.require;

/**
 * @author: PierreLuo
 * @date: 2019-06-25
 */
public class NRC721FullBase extends NRC721EnumerableBase {

    private String name;
    private String symbol;
    private Map<BigInteger, String> tokenURIs = new HashMap<BigInteger, String>();

    public NRC721FullBase(String name, String symbol) {
        // 由于Java没有多继承，所以挑选实现逻辑较少的INRC721Metadata在Full类里重新实现一次
        super.registerInterface("INRC721Metadata");
        this.name = name;
        this.symbol = symbol;
    }

    @View
    public String name() {
        return name;
    }

    @View
    public String symbol() {
        return symbol;
    }

    @View
    public String tokenURI(BigInteger tokenId) {
        require(exists(tokenId), "NRC721Metadata: URI query for nonexistent token");
        return tokenURIs.get(tokenId);
    }

    protected void setTokenURI(BigInteger tokenId, String uri) {
        require(exists(tokenId), "NRC721Metadata: URI set of nonexistent token");
        tokenURIs.put(tokenId, uri);
    }

    @Override
    protected void burnBase(Address owner, BigInteger tokenId) {
        super.burnBase(owner, tokenId);
        tokenURIs.remove(tokenId);
    }

    protected void mintWithTokenURIBase(Address to, BigInteger tokenId, String tokenURI) {
        this.setTokenURI(tokenId, tokenURI);
        super.mintBase(to, tokenId);
    }
}
