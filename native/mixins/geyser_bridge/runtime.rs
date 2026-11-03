use std::sync::atomic::{AtomicBool, Ordering};

static STARTED: AtomicBool = AtomicBool::new(false);

/// Called from Horizon authoritative server-start hook.
pub fn server_start(dedicated: bool) {
    if STARTED.swap(true, Ordering::AcqRel) {
        return;
    }
    eprintln!(
        "[geyser_bridge] Horizon server starting (dedicated={dedicated}); native bridge bootstrap loaded"
    );
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn bootstrap_is_idempotent() {
        assert!(!STARTED.swap(false, Ordering::AcqRel));
        server_start(false);
        server_start(false);
        assert!(STARTED.load(Ordering::Acquire));
        STARTED.store(false, Ordering::Release);
    }
}