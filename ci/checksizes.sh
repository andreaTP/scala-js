#!/bin/sh

BASEDIR="`dirname $0`/.."

FULLVER="$1"

case $FULLVER in
  2.11.12)
    VER=2.11
    ;;
  2.12.6)
    VER=2.12
    ;;
  2.13.0-M3)
    VER=2.13.0-M3
    ;;
  2.13.0-M4)
    VER=2.13.0-M4
    ;;
  2.11.0|2.11.1|2.11.2|2.11.4|2.11.5|2.11.6|2.11.7|2.11.8|2.11.11|2.12.1|2.12.2|2.12.3|2.12.4|2.12.5)
    echo "Ignoring checksizes for Scala $FULLVER"
    exit 0
    ;;
esac

REVERSI_PREOPT="$BASEDIR/examples/reversi/target/scala-$VER/reversi-fastopt.js"
REVERSI_OPT="$BASEDIR/examples/reversi/target/scala-$VER/reversi-opt.js"

REVERSI_PREOPT_SIZE=$(stat '-c%s' "$REVERSI_PREOPT")
REVERSI_OPT_SIZE=$(stat '-c%s' "$REVERSI_OPT")

gzip -c "$REVERSI_PREOPT" > "$REVERSI_PREOPT.gz"
gzip -c "$REVERSI_OPT" > "$REVERSI_OPT.gz"

REVERSI_PREOPT_GZ_SIZE=$(stat '-c%s' "$REVERSI_PREOPT.gz")
REVERSI_OPT_GZ_SIZE=$(stat '-c%s' "$REVERSI_OPT.gz")

case $FULLVER in
  2.11.12)
    REVERSI_PREOPT_EXPECTEDSIZE=509000
    REVERSI_OPT_EXPECTEDSIZE=116000
    REVERSI_PREOPT_GZ_EXPECTEDSIZE=69000
    REVERSI_OPT_GZ_EXPECTEDSIZE=30000
    ;;
  2.12.6)
    REVERSI_PREOPT_EXPECTEDSIZE=593000
    REVERSI_OPT_EXPECTEDSIZE=135000
    REVERSI_PREOPT_GZ_EXPECTEDSIZE=71000
    REVERSI_OPT_GZ_EXPECTEDSIZE=30000
    ;;
  2.13.0-M3)
    REVERSI_PREOPT_EXPECTEDSIZE=605000
    REVERSI_OPT_EXPECTEDSIZE=138000
    REVERSI_PREOPT_GZ_EXPECTEDSIZE=74000
    REVERSI_OPT_GZ_EXPECTEDSIZE=32000
    ;;
  2.13.0-M4)
    REVERSI_PREOPT_EXPECTEDSIZE=559000
    REVERSI_OPT_EXPECTEDSIZE=128000
    REVERSI_PREOPT_GZ_EXPECTEDSIZE=75000
    REVERSI_OPT_GZ_EXPECTEDSIZE=33000
    ;;
esac

echo "Checksizes: Scala version: $FULLVER"
echo "Reversi preopt size = $REVERSI_PREOPT_SIZE (expected $REVERSI_PREOPT_EXPECTEDSIZE)"
echo "Reversi opt size = $REVERSI_OPT_SIZE (expected $REVERSI_OPT_EXPECTEDSIZE)"
echo "Reversi preopt gzip size = $REVERSI_PREOPT_GZ_SIZE (expected $REVERSI_PREOPT_GZ_EXPECTEDSIZE)"
echo "Reversi opt gzip size = $REVERSI_OPT_GZ_SIZE (expected $REVERSI_OPT_GZ_EXPECTEDSIZE)"

[ "$REVERSI_PREOPT_SIZE" -le "$REVERSI_PREOPT_EXPECTEDSIZE" ] && \
  [ "$REVERSI_OPT_SIZE" -le "$REVERSI_OPT_EXPECTEDSIZE" ] && \
  [ "$REVERSI_PREOPT_GZ_SIZE" -le "$REVERSI_PREOPT_GZ_EXPECTEDSIZE" ] && \
  [ "$REVERSI_OPT_GZ_SIZE" -le "$REVERSI_OPT_GZ_EXPECTEDSIZE" ]
