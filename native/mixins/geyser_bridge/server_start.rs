// Runs before Horizon integrated or dedicated server starts.
fn server_start(dedicated: bool) {
    crate::game::geyser_bridge::server_start(dedicated);
}