package net.chwthewke.protobuf;

import google.protobuf.compiler.Plugin.CodeGeneratorRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

public class DumpPlugin {
    public static void main( final String[ ] args ) {
        new DumpPlugin( ).run( );
    }

    private void run( ) {
        try
        {
            final CodeGeneratorRequest request = CodeGeneratorRequest.parseFrom( CodedInputStream.newInstance( System.in ) );
            try
            {
                dump( request );
            }
            catch ( final IOException e )
            {
                log( Level.ERROR, "An exception occured while dumping the request: " + e.getMessage( ) );
            }
        }
        catch ( final IOException e )
        {
            log( Level.ERROR, "An exception occured while reading the request: " + e.getMessage( ) );
        }

        try
        {
            CodeGeneratorRequest.getDefaultInstance( ).writeTo( CodedOutputStream.newInstance( System.out ) );
        }
        catch ( final Exception e )
        {
            log( Level.ERROR, "An exception occured while writing the response: " + e.getMessage( ) );
        }
    }

    private void dump( final CodeGeneratorRequest request ) throws IOException {
        final File outFile = new File( "dump.cgr" );
        final FileOutputStream out = new FileOutputStream( outFile );
        try
        {
            request.writeTo( CodedOutputStream.newInstance( out ) );
        }
        finally
        {
            out.close( );
        }
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
