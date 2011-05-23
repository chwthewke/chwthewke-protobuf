package net.chwthewke.protobuf;

import google.protobuf.compiler.Plugin.CodeGeneratorRequest;
import google.protobuf.compiler.Plugin.CodeGeneratorResponse;
import google.protobuf.compiler.Plugin.CodeGeneratorResponse.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class DumpPlugin {
    public static void main( final String[ ] args ) {
        new DumpPlugin( ).run( );
    }

    private void run( ) {
        try
        {
            final CodeGeneratorRequest request = CodeGeneratorRequest.parseFrom( System.in );
            computeAndWriteResponse( request );
        }
        catch ( final IOException e )
        {
            log( Level.ERROR, "An exception occured while reading the request: " + e.getMessage( ) );
        }

    }

    private void computeAndWriteResponse( final CodeGeneratorRequest request ) {
        try
        {
            final CodeGeneratorResponse response = dump( request );
            writeResponse( response );
        }
        catch ( final IOException e )
        {
            log( Level.ERROR, "An exception occured while dumping the request: " + e.getMessage( ) );
        }
    }

    private void writeResponse( final CodeGeneratorResponse response ) {
        try
        {
            response.writeTo( System.out );
        }
        catch ( final Exception e )
        {
            log( Level.ERROR, "An exception occured while writing the response: " + e.getMessage( ) );
        }
    }

    private CodeGeneratorResponse dump( final CodeGeneratorRequest request ) throws IOException {

        final ByteArrayOutputStream sink = new ByteArrayOutputStream( );
        request.writeTo( sink );

        final String dump = new String( Base64.encodeBase64Chunked( sink.toByteArray( ) ) );
        return CodeGeneratorResponse.newBuilder( )
            .addFile( File.newBuilder( )
                .setName( "dump.cgr" )
                .setContent( dump ) )
            .build( );
    }

    void log( final Level level, final String message ) {
        System.err.println( String.format( "%5s - %s", level, message ) );
    }

    private enum Level {
        ERROR,
        WARN,
        INFO
    }
}
