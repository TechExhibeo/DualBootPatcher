// automatically generated, do not modify

package mbtool.daemon.v3;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class FileCloseRequest extends Table {
  public static FileCloseRequest getRootAsFileCloseRequest(ByteBuffer _bb) { return getRootAsFileCloseRequest(_bb, new FileCloseRequest()); }
  public static FileCloseRequest getRootAsFileCloseRequest(ByteBuffer _bb, FileCloseRequest obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__init(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public FileCloseRequest __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; return this; }

  public int id() { int o = __offset(4); return o != 0 ? bb.getInt(o + bb_pos) : 0; }

  public static int createFileCloseRequest(FlatBufferBuilder builder,
      int id) {
    builder.startObject(1);
    FileCloseRequest.addId(builder, id);
    return FileCloseRequest.endFileCloseRequest(builder);
  }

  public static void startFileCloseRequest(FlatBufferBuilder builder) { builder.startObject(1); }
  public static void addId(FlatBufferBuilder builder, int id) { builder.addInt(0, id, 0); }
  public static int endFileCloseRequest(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
};

