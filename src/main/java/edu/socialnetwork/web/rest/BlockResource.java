package edu.socialnetwork.web.rest;
import edu.socialnetwork.domain.Block;
import edu.socialnetwork.service.BlockService;
import edu.socialnetwork.web.rest.errors.BadRequestAlertException;
import edu.socialnetwork.web.rest.util.HeaderUtil;
import edu.socialnetwork.web.rest.util.PaginationUtil;
import edu.socialnetwork.service.dto.BlockCriteria;
import edu.socialnetwork.service.BlockQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Block.
 */
@RestController
@RequestMapping("/api")
public class BlockResource {

    private final Logger log = LoggerFactory.getLogger(BlockResource.class);

    private static final String ENTITY_NAME = "block";

    private final BlockService blockService;

    private final BlockQueryService blockQueryService;

    public BlockResource(BlockService blockService, BlockQueryService blockQueryService) {
        this.blockService = blockService;
        this.blockQueryService = blockQueryService;
    }

    /**
     * POST  /blocks : Create a new block.
     *
     * @param block the block to create
     * @return the ResponseEntity with status 201 (Created) and with body the new block, or with status 400 (Bad Request) if the block has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blocks")
    public ResponseEntity<Block> createBlock(@Valid @RequestBody Block block) throws URISyntaxException {
        log.debug("REST request to save Block : {}", block);
        if (block.getId() != null) {
            throw new BadRequestAlertException("A new block cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Block result = blockService.save(block);
        return ResponseEntity.created(new URI("/api/blocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /blocks : Updates an existing block.
     *
     * @param block the block to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated block,
     * or with status 400 (Bad Request) if the block is not valid,
     * or with status 500 (Internal Server Error) if the block couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/blocks")
    public ResponseEntity<Block> updateBlock(@Valid @RequestBody Block block) throws URISyntaxException {
        log.debug("REST request to update Block : {}", block);
        if (block.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Block result = blockService.save(block);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, block.getId().toString()))
            .body(result);
    }

    /**
     * GET  /blocks : get all the blocks.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of blocks in body
     */
    @GetMapping("/blocks")
    public ResponseEntity<List<Block>> getAllBlocks(BlockCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Blocks by criteria: {}", criteria);
        Page<Block> page = blockQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blocks");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /blocks/count : count all the blocks.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/blocks/count")
    public ResponseEntity<Long> countBlocks(BlockCriteria criteria) {
        log.debug("REST request to count Blocks by criteria: {}", criteria);
        return ResponseEntity.ok().body(blockQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /blocks/:id : get the "id" block.
     *
     * @param id the id of the block to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the block, or with status 404 (Not Found)
     */
    @GetMapping("/blocks/{id}")
    public ResponseEntity<Block> getBlock(@PathVariable Long id) {
        log.debug("REST request to get Block : {}", id);
        Optional<Block> block = blockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(block);
    }

    /**
     * DELETE  /blocks/:id : delete the "id" block.
     *
     * @param id the id of the block to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blocks/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        log.debug("REST request to delete Block : {}", id);
        blockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
