/*
 * Copyright (c) 2026 Horizon-GeyserBridge contributors.
 * Licensed under the MIT License; see the repository LICENSE.
 */
package org.geysermc.geyser.platform.standalone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Discovers Horizon's Open to LAN listener using the same multicast announcement
 * as the Minecraft Java LAN server browser.
 *
 * Horizon sends [MOTD]...[/MOTD][AD]port[/AD] to 224.0.2.60:4445 every 1.5s.
 * The announced port is the actual TCP listener chosen by Horizon (including
 * when the configured port was zero); it must not be guessed or scanned.
 */
final class HorizonLanDiscovery {
    private static final InetAddress GROUP;
    private static final int DISCOVERY_PORT = 4445;
    private static final Pattern ANNOUNCEMENT = Pattern.compile(
        "^\\[MOTD\\](.*?)\\[/MOTD\\]\\[AD\\]([0-9]{1,5})\\[/AD\\]$"
    );

    static {
        try {
            GROUP = InetAddress.getByName("224.0.2.60");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    record Target(String address, int port, String motd) {}

    private HorizonLanDiscovery() {}

    static Target await() throws IOException {
        System.out.println("Waiting for Horizon: open the world menu and select Open to LAN...");
        try (MulticastSocket socket = new MulticastSocket(null)) {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(DISCOVERY_PORT));
            socket.joinGroup(GROUP);
            byte[] buffer = new byte[2048];
            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress sender = packet.getAddress();
                // Do not accept a public Internet address as a LAN target.
                if (!(sender.isSiteLocalAddress() || sender.isLinkLocalAddress()
                    || sender.isLoopbackAddress())) {
                    continue;
                }
                String message = new String(packet.getData(), packet.getOffset(),
                    packet.getLength(), StandardCharsets.UTF_8).trim();
                Matcher matcher = ANNOUNCEMENT.matcher(message);
                if (!matcher.matches()) {
                    continue;
                }
                int port;
                try {
                    port = Integer.parseInt(matcher.group(2));
                } catch (NumberFormatException ignored) {
                    continue;
                }
                if (port < 1 || port > 65535) {
                    continue;
                }
                return new Target(sender.getHostAddress(), port, matcher.group(1));
            }
        }
        throw new IOException("discovery stopped before a Horizon world was found");
    }
}
