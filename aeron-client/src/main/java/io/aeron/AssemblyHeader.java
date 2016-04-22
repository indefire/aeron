/*
 * Copyright 2015 - 2016 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.aeron;

import io.aeron.logbuffer.Header;
import io.aeron.protocol.DataHeaderFlyweight;

import static io.aeron.logbuffer.FrameDescriptor.UNFRAGMENTED;

/**
 * Extends the base header to allow for a message to be reassembled from fragmented frames.
 */
public class AssemblyHeader extends Header
{
    private int frameLength;

    public AssemblyHeader reset(final Header base, final int msgLength)
    {
        positionBitsToShift(base.positionBitsToShift());
        initialTermId(base.initialTermId());
        offset(base.offset());
        buffer(base.buffer());
        frameLength = msgLength + DataHeaderFlyweight.HEADER_LENGTH;

        return this;
    }

    public int frameLength()
    {
        return frameLength;
    }

    public byte flags()
    {
        return (byte)(super.flags() | UNFRAGMENTED);
    }

    public int termOffset()
    {
        return offset() - (frameLength - super.frameLength());
    }
}
